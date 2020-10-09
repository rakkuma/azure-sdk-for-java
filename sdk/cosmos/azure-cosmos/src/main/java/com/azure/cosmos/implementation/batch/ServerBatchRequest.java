// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.implementation.batch;

import com.azure.cosmos.implementation.JsonSerializable;
import com.azure.cosmos.implementation.Utils;
import com.azure.cosmos.implementation.apachecommons.collections.list.UnmodifiableList;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.nio.ByteBuffer;
import java.util.List;

import static com.azure.cosmos.implementation.guava25.base.Preconditions.checkNotNull;
import static com.azure.cosmos.implementation.guava25.base.Preconditions.checkState;

/**
 * This class represents a server batch request.
 */
public abstract class ServerBatchRequest {

    private final int maxBodyLength;
    private final int maxOperationCount;

    private ByteBuffer requestBody;
    private List<ItemBatchOperation<?>> operations;
    private boolean isAtomicBatch = false;
    private boolean shouldContinueOnError = false;

    /**
     * Initializes a new {@link ServerBatchRequest request} instance.
     *
     * @param maxBodyLength Maximum length allowed for the request body.
     * @param maxOperationCount Maximum number of operations allowed in the request.
     */
    ServerBatchRequest(int maxBodyLength, int maxOperationCount) {
        this.maxBodyLength = maxBodyLength;
        this.maxOperationCount = maxOperationCount;
    }

    /**
     * Adds as many operations as possible from the given list of operations.
     * TODO(rakkuma): Similarly for hybrid row, request needs to be parsed to create a request body in any form.
     * Issue: https://github.com/Azure/azure-sdk-for-java/issues/15856
     *
     * Operations are added in order while ensuring the request body never exceeds {@link #maxBodyLength}.
     *
     * @param operations operations to be added; read-only.
     *
     * @return Any pending operations that were not included in the request.
     */
    final List<ItemBatchOperation<?>> createBodyOfBatchRequest(final List<ItemBatchOperation<?>> operations) {

        checkNotNull(operations, "expected non-null operations");

        int totalSerializedLength = 0;
        int totalOperationCount = 0;

        final ArrayNode arrayNode =  Utils.getSimpleObjectMapper().createArrayNode();

        for(ItemBatchOperation<?> operation : operations) {

            final JsonSerializable operationJsonSerializable = ItemBatchOperation.writeOperation(operation);

            // TODO(rakkuma): If the string contains unicode the byte encoding len will be more. Fix it.
            // Issue: https://github.com/Azure/azure-sdk-for-java/issues/16112
            final int operationSerializedLength = operationJsonSerializable.toString().length();

            if (totalOperationCount != 0 &&
                (totalSerializedLength + operationSerializedLength > this.maxBodyLength || totalOperationCount + 1 > this.maxOperationCount)) {
                // Apply the limit only if at least there is one operation in selected operations.
                break;
            }

            totalSerializedLength += operationSerializedLength;
            totalOperationCount++;

            arrayNode.add(operationJsonSerializable.getPropertyBag());
        }

        this.requestBody = ByteBuffer.wrap(Utils.getUTF8Bytes(arrayNode.toString()));
        this.operations = operations.subList(0, totalOperationCount);

        return operations.subList(totalOperationCount, operations.size());
    }

    public final ByteBuffer getRequestBodyAsByteBuffer() {
        checkState(this.requestBody != null, "expected non-null body");

        return this.requestBody;
    }

    /**
     * Gets the list of {@link ItemBatchOperation operations} in this {@link ServerBatchRequest batch request}.
     *
     * The list returned by this method is unmodifiable.
     *
     * @return the list of {@link ItemBatchOperation operations} in this {@link ServerBatchRequest batch request}.
     */
    public final List<ItemBatchOperation<?>> getOperations() {
        return UnmodifiableList.unmodifiableList(this.operations);
    }

    public boolean isAtomicBatch() {
        return this.isAtomicBatch;
    }

    void setAtomicBatch(boolean atomicBatch) {
        this.isAtomicBatch = atomicBatch;
    }

    public boolean isShouldContinueOnError() {
        return this.shouldContinueOnError;
    }

    void setShouldContinueOnError(boolean shouldContinueOnError) {
        this.shouldContinueOnError = shouldContinueOnError;
    }
}
