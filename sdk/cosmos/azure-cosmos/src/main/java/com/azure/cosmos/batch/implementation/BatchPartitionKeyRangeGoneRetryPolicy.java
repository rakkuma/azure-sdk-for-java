// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.batch.implementation;

import com.azure.cosmos.BridgeInternal;
import com.azure.cosmos.CosmosException;
import com.azure.cosmos.implementation.DocumentClientRetryPolicy;
import com.azure.cosmos.implementation.HttpConstants.StatusCodes;
import com.azure.cosmos.implementation.HttpConstants.SubStatusCodes;
import com.azure.cosmos.implementation.RxDocumentServiceRequest;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

import static com.azure.cosmos.implementation.guava25.base.Preconditions.checkNotNull;

/**
 * Used only in the context of Bulk Stream operations.
 *
 * See {@link BatchAsyncBatcher}
 * See {@link ItemBatchOperationContext}
 */
public final class BatchPartitionKeyRangeGoneRetryPolicy extends DocumentClientRetryPolicy {

    private static final int MAX_RETRIES = 1;

    private final DocumentClientRetryPolicy nextRetryPolicy;
    private int attemptedRetries;

    public BatchPartitionKeyRangeGoneRetryPolicy(DocumentClientRetryPolicy nextRetryPolicy) {
        this.nextRetryPolicy = nextRetryPolicy;
    }

    @Override
    public Mono<ShouldRetryResult> shouldRetry(final Exception exception) {

        checkNotNull(exception, "expected non-null exception");

        if (exception instanceof CosmosException) {

            final CosmosException cosmosException = (CosmosException) exception;

            final ShouldRetryResult result = this.shouldRetryInternal(
                cosmosException.getStatusCode(),
                cosmosException.getSubStatusCode());

            if (result != null) {
                return Mono.just(result);
            }
        }

        if (this.nextRetryPolicy == null) {
            return Mono.just(ShouldRetryResult.noRetry());
        }

        return this.nextRetryPolicy.shouldRetry(exception);
    }

    public Mono<ShouldRetryResult> shouldRetry(int statusCode, int subStatusCode, Map<String, String> responseHeaders) {

        final ShouldRetryResult result = this.shouldRetryInternal(
            statusCode,
            subStatusCode);

        if (result != null) {
            return Mono.just(result);
        }

        if (this.nextRetryPolicy == null) {
            return Mono.just(ShouldRetryResult.noRetry());
        }

        // Create CosmosException for the next retry policy to understand:
        CosmosException exception = BridgeInternal.createCosmosException(statusCode, null, responseHeaders);

        return this.nextRetryPolicy.shouldRetry(exception);
    }

    public void onBeforeSendRequest(RxDocumentServiceRequest request) {
        this.nextRetryPolicy.onBeforeSendRequest(request);
    }

    private ShouldRetryResult shouldRetryInternal(int statusCode, int subStatusCode) {

        if (statusCode == StatusCodes.GONE
            && (subStatusCode == SubStatusCodes.PARTITION_KEY_RANGE_GONE || subStatusCode == SubStatusCodes.NAME_CACHE_IS_STALE)
            && this.attemptedRetries < MAX_RETRIES) {
            this.attemptedRetries++;
            return ShouldRetryResult.retryAfter(Duration.ZERO);
        }

        return null;
    }
}
