// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.implementation.batch;

import com.azure.cosmos.CosmosDiagnostics;
import com.azure.cosmos.implementation.MetadataDiagnosticsContext;

final class BulkOperationContext {
    private BulkOperationRetryPolicy bulkOperationRetryPolicy;
    private MetadataDiagnosticsContext metadataDiagnosticsContext;
    private CosmosDiagnostics cosmosDiagnostics;

    BulkOperationContext() {
        this.metadataDiagnosticsContext = new MetadataDiagnosticsContext();
        this.cosmosDiagnostics = null;
    }

    void setBulkOperationRetryPolicy(BulkOperationRetryPolicy bulkOperationRetryPolicy) {
        this.bulkOperationRetryPolicy = bulkOperationRetryPolicy;
    }

    BulkOperationRetryPolicy getRetryPolicy() {
        return bulkOperationRetryPolicy;
    }

    void setMetadataDiagnosticsContext(MetadataDiagnosticsContext metadataDiagnosticsContext) {
        this.metadataDiagnosticsContext = metadataDiagnosticsContext;
    }

    MetadataDiagnosticsContext getMetadataDiagnosticsContext() {
        return metadataDiagnosticsContext;
    }

    CosmosDiagnostics getCosmosDiagnostics() {
        return cosmosDiagnostics;
    }

    void setCosmosDiagnostics(CosmosDiagnostics cosmosDiagnostics) {
        this.cosmosDiagnostics = cosmosDiagnostics;
    }
}
