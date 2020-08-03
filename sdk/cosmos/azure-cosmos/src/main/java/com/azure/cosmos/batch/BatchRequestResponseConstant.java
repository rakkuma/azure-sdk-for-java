// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.batch;

public class BatchRequestResponseConstant {

    // Size limits:
    public static final int MAX_DIRECT_MODE_BATCH_REQUEST_BODY_SIZE_IN_BYTES = 2202010;
    public static final int MAX_OPERATIONS_IN_DIRECT_MODE_BATCH_REQUEST = 100;

    public static final String FIELD_OPERATION_TYPE = "operationType";
    public static final String FIELD_RESOURCE_TYPE = "resourceType";
    public static final String FIELD_TIME_TO_LIVE_IN_SECONDS = "timeToLiveInSeconds";
    public static final String FIELD_ID = "id";
    public static final String FIELD_INDEXING_DIRECTIVE = "indexingDirective";
    public static final String FIELD_IF_MATCH = "ifMatch";
    public static final String FIELD_IF_NONE_MATCH = "ifNoneMatch";
    public static final String FIELD_PARTITION_KEY = "partitionKey";
    public static final String FIELD_RESOURCE_BODY = "resourceBody";
    public static final String FIELD_BINARY_ID = "binaryId";
    public static final String FIELD_EFFECTIVE_PARTITIONKEY = "effectivePartitionKey";
    public static final String FIELD_STATUS_CODE = "statusCode";
    public static final String FIELD_SUBSTATUS_CODE = "subStatusCode";
    public static final String FIELD_REQUEST_CHARGE = "requestCharge";
    public static final String FIELD_RETRY_AFTER_MILLISECONDS = "retryAfterMilliseconds";
    public static final String FIELD_ETAG = "eTag";
    public static final String FIELD_MINIMAL_RETURN_PREFERENCE = "minimalReturnPreference";
    public static final String FIELD_IS_CLIENTENCRYPTED = "isClientEncrypted";

    // Batch supported operation type for json
    public static final String OPERATION_CREATE = "Create";
    public static final String OPERATION_PATCH = "Patch";
    public static final String OPERATION_READ = "Read";
    public static final String OPERATION_UPSERT = "Upsert";
    public static final String OPERATION_DELETE = "Delete";
    public static final String OPERATION_REPLACE = "Replace";
}
