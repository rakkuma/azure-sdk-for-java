// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos;

import com.azure.cosmos.implementation.RequestOptions;
import com.azure.cosmos.util.Beta;

/**
 * Encapsulates options that can be specified for an operation used in Bulk execution. It can be passed while
 * creating bulk request using {@link BulkOperations}.
 */
@Beta(Beta.SinceVersion.V4_8_0)
public final class BulkItemRequestOptions {
    private ConsistencyLevel consistencyLevel;
    private String sessionToken;

    /**
     * Gets the consistency level required for the request.
     *
     * @return the consistency level.
     */
    ConsistencyLevel getConsistencyLevel() {
        return consistencyLevel;
    }

    /**
     * Sets the consistency level required for the request.
     *
     * @param consistencyLevel the consistency level.
     * @return the BulkItemRequestOptions.
     */
    BulkItemRequestOptions setConsistencyLevel(ConsistencyLevel consistencyLevel) {
        this.consistencyLevel = consistencyLevel;
        return this;
    }

    /**
     * Gets the token for use with session consistency.
     *
     * @return the session token.
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * Sets the token for use with session consistency.
     *
     * @param sessionToken the session token.
     * @return the BulkItemRequestOptions.
     */
    public BulkItemRequestOptions setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
        return this;
    }

    RequestOptions toRequestOptions() {
        final RequestOptions requestOptions = new RequestOptions();
        requestOptions.setConsistencyLevel(getConsistencyLevel());
        requestOptions.setSessionToken(sessionToken);
        return requestOptions;
    }
}
