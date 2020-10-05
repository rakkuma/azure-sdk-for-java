// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos;

import com.azure.cosmos.implementation.HttpConstants;
import com.azure.cosmos.implementation.ISessionToken;
import com.azure.cosmos.implementation.guava25.base.Function;
import com.azure.cosmos.models.CosmosItemResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.UUID;

import static com.azure.cosmos.implementation.batch.BatchRequestResponseConstant.MAX_DIRECT_MODE_BATCH_REQUEST_BODY_SIZE_IN_BYTES;
import static com.azure.cosmos.implementation.batch.BatchRequestResponseConstant.MAX_OPERATIONS_IN_DIRECT_MODE_BATCH_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionalBatchTest extends BatchTestBase {

    private CosmosClient batchClient;
    private CosmosContainer batchContainer;

    @Factory(dataProvider = "simpleClientBuildersWithDirect")
    public TransactionalBatchTest(CosmosClientBuilder clientBuilder) {
        super(clientBuilder);
    }

    @BeforeClass(groups = {"simple"}, timeOut = SETUP_TIMEOUT)
    public void before_TransactionalBatchTest() {
        assertThat(this.batchClient).isNull();
        this.batchClient = getClientBuilder().buildClient();
        CosmosAsyncContainer batchAsyncContainer = getSharedMultiPartitionCosmosContainer(this.batchClient.asyncClient());
        batchContainer = batchClient.getDatabase(batchAsyncContainer.getDatabase().getId()).getContainer(batchAsyncContainer.getId());
    }

    @AfterClass(groups = {"simple"}, timeOut = SHUTDOWN_TIMEOUT, alwaysRun = true)
    public void afterClass() {
        safeCloseSyncClient(this.batchClient);
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT)
    public void batchOrdered() {
        CosmosContainer container = this.batchContainer;
        this.createJsonTestDocs(container);

        TestDoc firstDoc = this.populateTestDoc(this.partitionKey1);

        TestDoc replaceDoc = this.getTestDocCopy(firstDoc);
        replaceDoc.setCost(replaceDoc.getCost() + 1);

        TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
            TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
                .createItem(firstDoc)
                .replaceItem(replaceDoc.getId(), replaceDoc));

        this.verifyBatchProcessed(batchResponse, 2);

        assertThat(batchResponse.get(0).getStatusCode()).isEqualTo(HttpResponseStatus.CREATED.code());
        assertThat(batchResponse.get(1).getStatusCode()).isEqualTo(HttpResponseStatus.OK.code());

        // Ensure that the replace overwrote the doc from the first operation
        this.verifyByRead(container, replaceDoc);
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT)
    public void batchMultipleItemExecution() {
        CosmosContainer container = this.batchContainer;
        this.createJsonTestDocs(container);

        TestDoc firstDoc = this.populateTestDoc(this.partitionKey1);
        TestDoc replaceDoc = this.getTestDocCopy(firstDoc);
        replaceDoc.setCost(replaceDoc.getCost() + 1);

        EventDoc eventDoc1 = new EventDoc(UUID.randomUUID().toString(), 2, 4, "type1", this.partitionKey1);
        EventDoc readEventDoc = new EventDoc(UUID.randomUUID().toString(), 6, 14, "type2", this.partitionKey1);
        CosmosItemResponse<EventDoc> createResponse = container.createItem(readEventDoc, this.getPartitionKey(this.partitionKey1), null);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpResponseStatus.CREATED.code());

        TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
            TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
                .createItem(firstDoc)
                .createItem(eventDoc1)
                .replaceItem(replaceDoc.getId(), replaceDoc)
                .readItem(readEventDoc.getId()));

        this.verifyBatchProcessed(batchResponse, 4);

        assertThat(batchResponse.get(0).getStatusCode()).isEqualTo(HttpResponseStatus.CREATED.code());
        assertThat(batchResponse.getOperationResultAtIndex(0, TestDoc.class).getItem()).isEqualTo(firstDoc);

        assertThat(batchResponse.get(1).getStatusCode()).isEqualTo(HttpResponseStatus.CREATED.code());
        assertThat(batchResponse.getOperationResultAtIndex(1, EventDoc.class).getItem()).isEqualTo(eventDoc1);

        assertThat(batchResponse.get(2).getStatusCode()).isEqualTo(HttpResponseStatus.OK.code());
        assertThat(batchResponse.getOperationResultAtIndex(2, TestDoc.class).getItem()).isEqualTo(replaceDoc);

        assertThat(batchResponse.get(3).getStatusCode()).isEqualTo(HttpResponseStatus.OK.code());
        assertThat(batchResponse.getOperationResultAtIndex(3, EventDoc.class).getItem()).isEqualTo(readEventDoc);

        // Ensure that the replace overwrote the doc from the first operation
        this.verifyByRead(container, replaceDoc);
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT)
    public void batchItemETagTest() {
        CosmosContainer container = batchContainer;
        this.createJsonTestDocs(container);

        {
            BatchTestBase.TestDoc testDocToCreate = this.populateTestDoc(this.partitionKey1);

            BatchTestBase.TestDoc testDocToReplace = this.getTestDocCopy(this.TestDocPk1ExistingA);
            testDocToReplace.setCost(testDocToReplace.getCost() + 1);

            CosmosItemResponse<TestDoc> response = container.readItem(
                this.TestDocPk1ExistingA.getId(),
                this.getPartitionKey(this.partitionKey1),
                TestDoc.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpResponseStatus.OK.code());

            TransactionalBatchItemRequestOptions firstReplaceOptions = new TransactionalBatchItemRequestOptions();
            firstReplaceOptions.setIfMatchETag(response.getETag());

            TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
                TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
                    .createItem(testDocToCreate)
                    .replaceItem(testDocToReplace.getId(), testDocToReplace, firstReplaceOptions));

            this.verifyBatchProcessed(batchResponse, 2);

            assertThat(batchResponse.get(0).getStatusCode()).isEqualTo(HttpResponseStatus.CREATED.code());
            assertThat(batchResponse.get(1).getStatusCode()).isEqualTo(HttpResponseStatus.OK.code());

            // Ensure that the replace overwrote the doc from the first operation
            this.verifyByRead(container, testDocToCreate, batchResponse.get(0).getETag());
            this.verifyByRead(container, testDocToReplace, batchResponse.get(1).getETag());
        }

        {
            TestDoc testDocToReplace = this.getTestDocCopy(this.TestDocPk1ExistingB);
            testDocToReplace.setCost(testDocToReplace.getCost() + 1);

            TransactionalBatchItemRequestOptions replaceOptions = new TransactionalBatchItemRequestOptions();
            replaceOptions.setIfMatchETag(String.valueOf(this.getRandom().nextInt()));

            TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
                TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
                    .replaceItem(testDocToReplace.getId(), testDocToReplace, replaceOptions));

            this.verifyBatchProcessed(batchResponse, 1, HttpResponseStatus.PRECONDITION_FAILED);

            assertThat(batchResponse.get(0).getStatusCode()).isEqualTo(HttpResponseStatus.PRECONDITION_FAILED.code());

            // ensure the item was not updated
            this.verifyByRead(container, this.TestDocPk1ExistingB);
        }
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT)
    public void batchItemSessionTokenTest() {
        CosmosContainer container = batchContainer;
        this.createJsonTestDocs(container);

        TestDoc testDocToCreate = this.populateTestDoc(this.partitionKey1);

        BatchTestBase.TestDoc testDocToReplace = this.getTestDocCopy(this.TestDocPk1ExistingA);
        testDocToReplace.setCost(testDocToReplace.getCost() + 1);

        CosmosItemResponse<TestDoc> readResponse = container.readItem(
            this.TestDocPk1ExistingA.getId(),
            this.getPartitionKey(this.partitionKey1),
            TestDoc.class);

        assertThat(readResponse.getStatusCode()).isEqualTo(HttpResponseStatus.OK.code());

        ISessionToken beforeRequestSessionToken = this.getSessionToken(readResponse.getResponseHeaders().get(HttpConstants.HttpHeaders.SESSION_TOKEN));

        TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
            TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
                .createItem(testDocToCreate)
                .replaceItem(testDocToReplace.getId(), testDocToReplace));

        this.verifyBatchProcessed(batchResponse, 2);

        assertThat(batchResponse.get(0).getStatusCode()).isEqualTo(HttpResponseStatus.CREATED.code());
        assertThat(batchResponse.get(1).getStatusCode()).isEqualTo(HttpResponseStatus.OK.code());

        ISessionToken afterRequestSessionToken = this.getSessionToken(batchResponse.getResponseHeaders().get(HttpConstants.HttpHeaders.SESSION_TOKEN));
        assertThat(afterRequestSessionToken.getLSN())
            .as("Response session token should be more than request session token")
            .isGreaterThan(beforeRequestSessionToken.getLSN());
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT)
    public void batchWithTooManyOperationsTest() {
        int operationCount = MAX_OPERATIONS_IN_DIRECT_MODE_BATCH_REQUEST + 1;

        // Increase the doc size by a bit so all docs won't fit in one server request.
        TransactionalBatch batch = TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1));

        for (int i = 0; i < operationCount; i++) {
            batch.readItem("someId");
        }

        try {
            batchContainer.executeTransactionalBatch(batch);
            Assertions.fail("Should throw bad request exception");
        } catch (CosmosException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpResponseStatus.BAD_REQUEST.code());
        }
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT * 10)
    public void batchLargerThanServerRequest() {
        int operationCount = 20;
        int appxDocSize = (MAX_DIRECT_MODE_BATCH_REQUEST_BODY_SIZE_IN_BYTES * 11) / operationCount;

        // Increase the doc size by a bit so all docs won't fit in one server request.
        appxDocSize = (int)(appxDocSize * 1.05);
        TransactionalBatch batch = TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1));

        for (int i = 0; i < operationCount; i++) {
            TestDoc doc = this.populateTestDoc(this.partitionKey1, appxDocSize);
            batch.createItem(doc);
        }

        try {
            batchContainer.executeTransactionalBatch(batch);
            Assertions.fail("Should throw REQUEST_ENTITY_TOO_LARGE exception");
        } catch (CosmosException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE.code());
        }
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT * 10)
    public void batchServerResponseTooLarge() {
        int operationCount = 10;
        int appxDocSizeInBytes = 1 * 1024 * 1024;

        TestDoc doc = this.createJsonTestDoc(batchContainer, this.partitionKey1, appxDocSizeInBytes);

        TransactionalBatch batch = TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1));
        for (int i = 0; i < operationCount; i++) {
            batch.readItem(doc.getId());
        }

        TransactionalBatchResponse batchResponse = batchContainer.executeTransactionalBatch(batch);
        assertThat(batchResponse.getStatusCode()).isEqualTo(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE.code());
        assertThat(batchResponse.get(1).getStatusCode()).isEqualTo(HttpResponseStatus.FAILED_DEPENDENCY.code());
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT)
    public void batchReadsOnlyTest() {
        CosmosContainer container = batchContainer;
        this.createJsonTestDocs(container);

        TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
            TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
                .readItem(this.TestDocPk1ExistingA.getId())
                .readItem(this.TestDocPk1ExistingB.getId())
                .readItem(this.TestDocPk1ExistingC.getId()));

        this.verifyBatchProcessed(batchResponse, 3);

        assertThat(batchResponse.get(0).getStatusCode()).isEqualTo(HttpResponseStatus.OK.code());
        assertThat(batchResponse.get(1).getStatusCode()).isEqualTo(HttpResponseStatus.OK.code());
        assertThat(batchResponse.get(2).getStatusCode()).isEqualTo(HttpResponseStatus.OK.code());

        assertThat(batchResponse.getOperationResultAtIndex(0, TestDoc.class).getItem()).isEqualTo(this.TestDocPk1ExistingA);
        assertThat(batchResponse.getOperationResultAtIndex(1, TestDoc.class).getItem()).isEqualTo(this.TestDocPk1ExistingB);
        assertThat(batchResponse.getOperationResultAtIndex(2, TestDoc.class).getItem()).isEqualTo(this.TestDocPk1ExistingC);
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT)
    public void batchCrud() {
        CosmosContainer container = batchContainer;
        this.createJsonTestDocs(container);

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
                .deleteItem(this.TestDocPk1ExistingD.getId()));

        this.verifyBatchProcessed(batchResponse, 6);

        assertThat(batchResponse.get(0).getStatusCode()).isEqualTo(HttpResponseStatus.CREATED.code());
        assertThat(batchResponse.get(1).getStatusCode()).isEqualTo(HttpResponseStatus.OK.code());
        assertThat(batchResponse.get(2).getStatusCode()).isEqualTo(HttpResponseStatus.OK.code());
        assertThat(batchResponse.get(3).getStatusCode()).isEqualTo(HttpResponseStatus.CREATED.code());
        assertThat(batchResponse.get(4).getStatusCode()).isEqualTo(HttpResponseStatus.OK.code());
        assertThat(batchResponse.get(5).getStatusCode()).isEqualTo(HttpResponseStatus.NO_CONTENT.code());

        assertThat(batchResponse.getOperationResultAtIndex(1, TestDoc.class).getItem()).isEqualTo(this.TestDocPk1ExistingC);

        this.verifyByRead(container, testDocToCreate);
        this.verifyByRead(container, testDocToReplace);
        this.verifyByRead(container, testDocToUpsert);
        this.verifyByRead(container, anotherTestDocToUpsert);
        this.verifyNotFound(container, this.TestDocPk1ExistingD);
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT)
    public void batchWithInvalidCreateTest() {
        // partition key mismatch between doc and and value passed in to the operation
        this.runWithError(
            batchContainer,
            batch -> batch.createItem(this.populateTestDoc(UUID.randomUUID().toString())),
            HttpResponseStatus.BAD_REQUEST);
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT)
    public void batchWithReadOfNonExistentEntityTest() {
        this.runWithError(
            batchContainer,
            batch -> batch.readItem(UUID.randomUUID().toString()),
            HttpResponseStatus.NOT_FOUND);
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT)
    public void batchWithReplaceOfStaleEntity() {
        this.createJsonTestDocs(batchContainer);

        TestDoc staleTestDocToReplace = this.getTestDocCopy(this.TestDocPk1ExistingA);
        staleTestDocToReplace.setCost(staleTestDocToReplace.getCost() + 1);

        TransactionalBatchItemRequestOptions staleReplaceOptions = new TransactionalBatchItemRequestOptions();
        staleReplaceOptions.setIfMatchETag(UUID.randomUUID().toString());

        this.runWithError(
            batchContainer,
            batch -> batch.replaceItem(staleTestDocToReplace.getId(), staleTestDocToReplace, staleReplaceOptions),
            HttpResponseStatus.PRECONDITION_FAILED);

        // make sure the stale doc hasn't changed
        this.verifyByRead(batchContainer, this.TestDocPk1ExistingA);
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT)
    public void batchWithDeleteOfNonExistentEntity() {
        this.runWithError(
            batchContainer,
            batch -> batch.deleteItem(UUID.randomUUID().toString()),
            HttpResponseStatus.NOT_FOUND);
    }

    @Test(groups = {"simple"}, timeOut = TIMEOUT)
    public void batchWithCreateConflict() {
        this.createJsonTestDocs(batchContainer);

        // try to create a doc with id that already exists (should return a Conflict)
        TestDoc conflictingTestDocToCreate = this.getTestDocCopy(this.TestDocPk1ExistingA);
        conflictingTestDocToCreate.setCost(conflictingTestDocToCreate.getCost());

        this.runWithError(
            batchContainer,
            batch -> batch.createItem(conflictingTestDocToCreate),
            HttpResponseStatus.CONFLICT);

        // make sure the conflicted doc hasn't changed
        this.verifyByRead(batchContainer, this.TestDocPk1ExistingA);
    }


    private void runWithError(
        CosmosContainer container,
        Function<TransactionalBatch, TransactionalBatch> appendOperation,
        HttpResponseStatus expectedFailedOperationStatusCode) {

        TestDoc testDocToCreate = this.populateTestDoc(this.partitionKey1);
        TestDoc anotherTestDocToCreate = this.populateTestDoc(this.partitionKey1);

        TransactionalBatch batch = TransactionalBatch.createTransactionalBatch(this.getPartitionKey(this.partitionKey1))
            .createItem(testDocToCreate);

        appendOperation.apply(batch);

        TransactionalBatchResponse batchResponse = container.executeTransactionalBatch(
            batch.createItem(anotherTestDocToCreate));

        this.verifyBatchProcessed(batchResponse, 3, expectedFailedOperationStatusCode);

        assertThat(batchResponse.get(0).getStatusCode()).isEqualTo(HttpResponseStatus.FAILED_DEPENDENCY.code());
        assertThat(batchResponse.get(1).getStatusCode()).isEqualTo(expectedFailedOperationStatusCode.code());
        assertThat(batchResponse.get(2).getStatusCode()).isEqualTo(HttpResponseStatus.FAILED_DEPENDENCY.code());

        this.verifyNotFound(container, testDocToCreate);
        this.verifyNotFound(container, anotherTestDocToCreate);
    }

    private void verifyBatchProcessed(TransactionalBatchResponse batchResponse, int numberOfOperations) {
        this.verifyBatchProcessed(batchResponse, numberOfOperations, HttpResponseStatus.OK);
    }

    private void verifyBatchProcessed(TransactionalBatchResponse batchResponse, int numberOfOperations, HttpResponseStatus expectedStatusCode) {
        assertThat(batchResponse).isNotNull();
        assertThat(batchResponse.getStatusCode())
            .as("Batch server response had StatusCode {0} instead of {1} expected and had ErrorMessage {2}",
                batchResponse.getStatusCode(), expectedStatusCode.code())
            .isEqualTo(expectedStatusCode.code());

        assertThat(batchResponse.size()).isEqualTo(numberOfOperations);
        assertThat(batchResponse.getRequestCharge()).isPositive();
        assertThat(batchResponse.getDiagnostics().toString()).isNotEmpty();

        // Allow a delta since we round both the total charge and the individual operation
        // charges to 2 decimal places.
        assertThat(batchResponse.getRequestCharge())
            .isCloseTo(batchResponse.getResults().stream().mapToDouble(TransactionalBatchOperationResult::getRequestCharge).sum(),
            Offset.offset(0.1));
    }
}
