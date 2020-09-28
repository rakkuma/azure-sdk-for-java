// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos;

import com.azure.cosmos.implementation.HttpConstants;
import com.azure.cosmos.implementation.ISessionToken;
import com.azure.cosmos.implementation.apachecommons.lang.StringUtils;
import com.azure.cosmos.implementation.guava25.base.Function;
import com.azure.cosmos.models.CosmosItemResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.UUID;

import static com.azure.cosmos.implementation.batch.BatchRequestResponseConstant.MAX_OPERATIONS_IN_DIRECT_MODE_BATCH_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionalBatchTest extends BatchTestBase {

    private CosmosAsyncClient batchClient;
    private CosmosAsyncContainer batchContainer;
    private CosmosAsyncContainer sharedThroughputContainer;

    @Factory(dataProvider = "simpleClientBuildersWithDirect")
    public TransactionalBatchTest(CosmosClientBuilder clientBuilder) {
        super(clientBuilder);
    }

    @BeforeClass(groups = {"emulator"}, timeOut = SETUP_TIMEOUT)
    public void before_TransactionalBatchTest() {
        assertThat(this.batchClient).isNull();
        this.batchClient = getClientBuilder().buildAsyncClient();
        CosmosAsyncContainer asyncContainer = getSharedMultiPartitionCosmosContainer(this.batchClient);
        batchContainer = batchClient.getDatabase(asyncContainer.getDatabase().getId()).getContainer(asyncContainer.getId());
        sharedThroughputContainer = super.createSharedThroughputContainer(this.batchClient);
    }

    @AfterClass(groups = {"emulator"}, timeOut = SHUTDOWN_TIMEOUT, alwaysRun = true)
    public void afterClass() {
        assertThat(this.batchClient).isNotNull();

        if (sharedThroughputContainer != null) {
            // Delete the database created in this test
            sharedThroughputContainer.getDatabase().delete().block();
        }

        this.batchClient.close();
    }

    @Test(groups = {"emulator"}, timeOut = TIMEOUT)
    public void batchCrud() throws Exception {
         this.runCrudAsync(batchContainer);
    }

    @Test(groups = {"emulator"}, timeOut = TIMEOUT)
    public void batchSharedThroughputCrud() throws Exception {
        this.runCrudAsync(sharedThroughputContainer);
    }

    @Test(groups = {"emulator"}, timeOut = TIMEOUT)
    public void batchOrdered() {
        CosmosAsyncContainer container = batchContainer;
        this.createJsonTestDocsAsync(container);

        TestDoc firstDoc = this.populateTestDoc(this.partitionKey1);

        TestDoc replaceDoc = this.getTestDocCopy(firstDoc);
        replaceDoc.setCost(replaceDoc.getCost() + 1);

        TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
            TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
                .createItem(firstDoc)
                .replaceItem(replaceDoc.getId(), replaceDoc))
            .block();

        this.verifyBatchProcessed(batchResponse, 2);

        Assert.assertEquals(HttpResponseStatus.CREATED.code(), batchResponse.get(0).getResponseStatus());
        Assert.assertEquals(HttpResponseStatus.OK.code(), batchResponse.get(1).getResponseStatus());

        // Ensure that the replace overwrote the doc from the first operation
        this.verifyByReadAsync(container, replaceDoc);
    }

    @Test(groups = {"emulator"}, timeOut = TIMEOUT)
    public void batchItemETagAsync() {
        CosmosAsyncContainer container = batchContainer;
        this.createJsonTestDocsAsync(container);

        {
            BatchTestBase.TestDoc testDocToCreate = this.populateTestDoc(this.partitionKey1);

            BatchTestBase.TestDoc testDocToReplace = this.getTestDocCopy(this.TestDocPk1ExistingA);
            testDocToReplace.setCost(testDocToReplace.getCost() + 1);

            CosmosItemResponse<TestDoc> response = container.readItem(
                this.TestDocPk1ExistingA.getId(),
                this.getPartitionKey(this.partitionKey1),
                TestDoc.class).block();

            Assert.assertEquals(HttpResponseStatus.OK.code(), response.getStatusCode());

            TransactionalBatchItemRequestOptions firstReplaceOptions = new TransactionalBatchItemRequestOptions();
            firstReplaceOptions.setIfMatchETag(response.getETag());

            TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
                TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
                    .createItem(testDocToCreate)
                    .replaceItem(testDocToReplace.getId(), testDocToReplace, firstReplaceOptions))
                .block();

            this.verifyBatchProcessed(batchResponse, 2);

            Assert.assertEquals(HttpResponseStatus.CREATED.code(), batchResponse.get(0).getResponseStatus());
            Assert.assertEquals(HttpResponseStatus.OK.code(), batchResponse.get(1).getResponseStatus());

            // Ensure that the replace overwrote the doc from the first operation
            this.verifyByReadAsync(container, testDocToCreate, batchResponse.get(0).getETag());
            this.verifyByReadAsync(container, testDocToReplace, batchResponse.get(1).getETag());
        }

        {
            TestDoc testDocToReplace = this.getTestDocCopy(this.TestDocPk1ExistingB);
            testDocToReplace.setCost(testDocToReplace.getCost() + 1);

            TransactionalBatchItemRequestOptions replaceOptions = new TransactionalBatchItemRequestOptions();
            replaceOptions.setIfMatchETag(String.valueOf(this.getRandom().nextInt()));

            TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
                TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
                    .replaceItem(testDocToReplace.getId(), testDocToReplace, replaceOptions))
                .block();

            this.verifyBatchProcessed(batchResponse, 1, HttpResponseStatus.PRECONDITION_FAILED);

            Assert.assertEquals(HttpResponseStatus.PRECONDITION_FAILED.code(), batchResponse.get(0).getResponseStatus());

            // ensure the document was not updated
            this.verifyByReadAsync(container, this.TestDocPk1ExistingB);
        }
    }

    @Test(groups = {"emulator"}, timeOut = TIMEOUT)
    public void batchItemSessionTokenAsync() {
        CosmosAsyncContainer container = batchContainer;
        this.createJsonTestDocsAsync(container);

        TestDoc testDocToCreate = this.populateTestDoc(this.partitionKey1);

        BatchTestBase.TestDoc testDocToReplace = this.getTestDocCopy(this.TestDocPk1ExistingA);
        testDocToReplace.setCost(testDocToReplace.getCost() + 1);

        CosmosItemResponse<TestDoc> readResponse = container.readItem(
            this.TestDocPk1ExistingA.getId(),
            this.getPartitionKey(this.partitionKey1),
            TestDoc.class).block();

        Assert.assertEquals(HttpResponseStatus.OK.code(), readResponse.getStatusCode());

        ISessionToken beforeRequestSessionToken = this.getSessionToken(readResponse.getResponseHeaders().get(HttpConstants.HttpHeaders.SESSION_TOKEN));

        TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
            TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
                .createItem(testDocToCreate)
                .replaceItem(testDocToReplace.getId(), testDocToReplace))
            .block();

        this.verifyBatchProcessed(batchResponse, 2);

        Assert.assertEquals(HttpResponseStatus.CREATED.code(), batchResponse.get(0).getResponseStatus());
        Assert.assertEquals(HttpResponseStatus.OK.code(), batchResponse.get(1).getResponseStatus());

        ISessionToken afterRequestSessionToken = this.getSessionToken(batchResponse.getResponseHeaders().get(HttpConstants.HttpHeaders.SESSION_TOKEN));
        Assert.assertTrue(afterRequestSessionToken.getLSN() > beforeRequestSessionToken.getLSN(), "Response session token should be more than request session token");
    }

    @Test(groups = {"emulator"}, timeOut = TIMEOUT)
    public void batchWithTooManyOperationsAsync() {
        CosmosAsyncContainer container = batchContainer;
        int operationCount = MAX_OPERATIONS_IN_DIRECT_MODE_BATCH_REQUEST + 1;

        // Increase the doc size by a bit so all docs won't fit in one server request.
        TransactionalBatch batch = TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1));

        for (int i = 0; i < operationCount; i++) {
            batch.readItem("someId");
        }

        TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(batch).block();
        Assert.assertEquals(HttpResponseStatus.BAD_REQUEST.code(), batchResponse.getResponseStatus());
    }

    @Test(groups = {"emulator"}, timeOut = TIMEOUT)
    public void batchReadsOnlyAsync() throws Exception {
        CosmosAsyncContainer container = batchContainer;
        this.createJsonTestDocsAsync(container);

        TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
            TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
                .readItem(this.TestDocPk1ExistingA.getId())
                .readItem(this.TestDocPk1ExistingB.getId())
                .readItem(this.TestDocPk1ExistingC.getId()))
            .block();

        this.verifyBatchProcessed(batchResponse, 3);

        Assert.assertEquals(HttpResponseStatus.OK.code(),  batchResponse.get(0).getResponseStatus());
        Assert.assertEquals(HttpResponseStatus.OK.code(),  batchResponse.get(1).getResponseStatus());
        Assert.assertEquals(HttpResponseStatus.OK.code(),  batchResponse.get(2).getResponseStatus());

        Assert.assertEquals(this.TestDocPk1ExistingA, batchResponse.getOperationResultAtIndex(0, TestDoc.class).getItem());
        Assert.assertEquals(this.TestDocPk1ExistingB, batchResponse.getOperationResultAtIndex(1, TestDoc.class).getItem());
        Assert.assertEquals(this.TestDocPk1ExistingC, batchResponse.getOperationResultAtIndex(2, TestDoc.class).getItem());
    }

    private TransactionalBatchResponse runCrudAsync(CosmosAsyncContainer container) throws Exception {
        this.createJsonTestDocsAsync(container);

        BatchTestBase.TestDoc testDocToCreate = this.populateTestDoc(this.partitionKey1);
        BatchTestBase.TestDoc testDocToUpsert = this.populateTestDoc(this.partitionKey1);

        BatchTestBase.TestDoc anotherTestDocToUpsert = this.getTestDocCopy(this.TestDocPk1ExistingA);
        anotherTestDocToUpsert.setCost(anotherTestDocToUpsert.getCost() + 1);

        BatchTestBase.TestDoc testDocToReplace = this.getTestDocCopy(this.TestDocPk1ExistingB);
        testDocToReplace.setCost(testDocToReplace.getCost() + 1);

        // We run CRUD operations where all are expected to return HTTP 2xx.
        TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
            TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
                .createItem(testDocToCreate)
                .readItem(this.TestDocPk1ExistingC.getId())
                .replaceItem(testDocToReplace.getId(), testDocToReplace)
                .upsertItem(testDocToUpsert)
                .upsertItem(anotherTestDocToUpsert)
                .deleteItem(this.TestDocPk1ExistingD.getId()))
            .block();

        this.verifyBatchProcessed(batchResponse, 6);

        Assert.assertEquals(HttpResponseStatus.CREATED.code(), batchResponse.get(0).getResponseStatus());
        Assert.assertEquals(HttpResponseStatus.OK.code(), batchResponse.get(1).getResponseStatus());
        Assert.assertEquals(HttpResponseStatus.OK.code(), batchResponse.get(2).getResponseStatus());
        Assert.assertEquals(HttpResponseStatus.CREATED.code(), batchResponse.get(3).getResponseStatus());
        Assert.assertEquals(HttpResponseStatus.OK.code(), batchResponse.get(4).getResponseStatus());
        Assert.assertEquals(HttpResponseStatus.NO_CONTENT.code(), batchResponse.get(5).getResponseStatus());

        Assert.assertEquals(this.TestDocPk1ExistingC, batchResponse.getOperationResultAtIndex(1, TestDoc.class).getItem());

        this.verifyByReadAsync(container, testDocToCreate);
        this.verifyByReadAsync(container, testDocToReplace);
        this.verifyByReadAsync(container, testDocToUpsert);
        this.verifyByReadAsync(container, anotherTestDocToUpsert);
        this.verifyNotFoundAsync(container, this.TestDocPk1ExistingD);

        return batchResponse;
    }

    @Test(groups = {"emulator"}, timeOut = TIMEOUT)
    public void batchWithCreateConflictAsync() {
        this.runBatchWithCreateConflictAsync(batchContainer);
    }

    @Test(groups = {"emulator"}, timeOut = TIMEOUT)
    public void batchWithCreateConflictSharedThroughputAsync() {
        this.runBatchWithCreateConflictAsync(this.sharedThroughputContainer);
    }

    @Test(groups = {"emulator"}, timeOut = TIMEOUT)
    public void batchWithInvalidCreateAsync() {
        CosmosAsyncContainer container = batchContainer;

        // partition key mismatch between doc and and value passed in to the operation
        this.runWithErrorAsync(
            container,
            batch -> batch.createItem(this.populateTestDoc(UUID.randomUUID().toString())),
            HttpResponseStatus.BAD_REQUEST);
    }

    @Test(groups = {"emulator"}, timeOut = TIMEOUT)
    public void batchWithReadOfNonExistentEntityAsync() {
        CosmosAsyncContainer container = batchContainer;
        this.runWithErrorAsync(
            container,
            batch -> batch.readItem(UUID.randomUUID().toString()),
            HttpResponseStatus.NOT_FOUND);
    }

    @Test(groups = {"emulator"}, timeOut = TIMEOUT)
    public void batchWithReplaceOfStaleEntityAsync() {
        CosmosAsyncContainer container = batchContainer;
        this.createJsonTestDocsAsync(container);

        TestDoc staleTestDocToReplace = this.getTestDocCopy(this.TestDocPk1ExistingA);
        staleTestDocToReplace.setCost(staleTestDocToReplace.getCost() + 1);

        TransactionalBatchItemRequestOptions staleReplaceOptions = new TransactionalBatchItemRequestOptions();
        staleReplaceOptions.setIfMatchETag(UUID.randomUUID().toString());

        this.runWithErrorAsync(
            container,
            batch -> batch.replaceItem(staleTestDocToReplace.getId(), staleTestDocToReplace, staleReplaceOptions),
            HttpResponseStatus.PRECONDITION_FAILED);

        // make sure the stale doc hasn't changed
        this.verifyByReadAsync(container, this.TestDocPk1ExistingA);
    }

    @Test(groups = {"emulator"}, timeOut = TIMEOUT)
    public void batchWithDeleteOfNonExistentEntityAsync() {
        CosmosAsyncContainer container = batchContainer;

        this.runWithErrorAsync(
            container,
            batch -> batch.deleteItem(UUID.randomUUID().toString()),
            HttpResponseStatus.NOT_FOUND);
    }

    private void runBatchWithCreateConflictAsync(CosmosAsyncContainer container) {
        this.createJsonTestDocsAsync(container);

        // try to create a doc with id that already exists (should return a Conflict)
        TestDoc conflictingTestDocToCreate = this.getTestDocCopy(this.TestDocPk1ExistingA);
        conflictingTestDocToCreate.setCost(conflictingTestDocToCreate.getCost());

        this.runWithErrorAsync(
            container,
            batch -> batch.createItem(conflictingTestDocToCreate),
            HttpResponseStatus.CONFLICT);

        // make sure the conflicted doc hasn't changed
        this.verifyByReadAsync(container, this.TestDocPk1ExistingA);
    }


    private void runWithErrorAsync(
        CosmosAsyncContainer container,
        Function<TransactionalBatch, TransactionalBatch> appendOperation,
        HttpResponseStatus expectedFailedOperationStatusCode) {

        TestDoc testDocToCreate = this.populateTestDoc(this.partitionKey1);
        TestDoc anotherTestDocToCreate = this.populateTestDoc(this.partitionKey1);

        TransactionalBatch batch = TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
            .createItem(testDocToCreate);

        appendOperation.apply(batch);

        TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
            batch.createItem(anotherTestDocToCreate))
            .block();

        this.verifyBatchProcessed(batchResponse, 3, expectedFailedOperationStatusCode);

        Assert.assertEquals(HttpResponseStatus.FAILED_DEPENDENCY.code(), batchResponse.get(0).getResponseStatus());
        Assert.assertEquals(expectedFailedOperationStatusCode.code(), batchResponse.get(1).getResponseStatus());
        Assert.assertEquals(HttpResponseStatus.FAILED_DEPENDENCY.code(), batchResponse.get(2).getResponseStatus());

        this.verifyNotFoundAsync(container, testDocToCreate);
        this.verifyNotFoundAsync(container, anotherTestDocToCreate);
    }

    private void verifyBatchProcessed(TransactionalBatchResponse batchResponse, int numberOfOperations) {
        this.verifyBatchProcessed(batchResponse, numberOfOperations, HttpResponseStatus.OK);
    }

    private void verifyBatchProcessed(TransactionalBatchResponse batchResponse, int numberOfOperations, HttpResponseStatus expectedStatusCode) {
        Assert.assertNotNull(batchResponse);
        Assert.assertEquals(
            batchResponse.getResponseStatus(),
            expectedStatusCode.code(),
            "Batch server response had StatusCode {0} instead of {1} expected and had ErrorMessage {2}");

        Assert.assertEquals(numberOfOperations, batchResponse.size());
        Assert.assertTrue(batchResponse.getRequestCharge() > 0);
        Assert.assertTrue(StringUtils.isNotEmpty(batchResponse.getCosmosDiagnostics().toString()));

        // Allow a delta since we round both the total charge and the individual operation
        // charges to 2 decimal places.
        Assert.assertEquals(
            batchResponse.getRequestCharge(),
            batchResponse.stream().mapToDouble(result -> result.getRequestCharge()).sum(),
            0.1);
    }
}
