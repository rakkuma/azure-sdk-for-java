// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See License.txt in the project root for
// license information.
// 
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.management.appservice;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The ResponseMetaData model.
 */
@Fluent
public final class ResponseMetaData {
    /*
     * Class representing data source used by the detectors
     */
    @JsonProperty(value = "dataSource")
    private DataSource dataSource;

    /**
     * Get the dataSource property: Class representing data source used by the
     * detectors.
     * 
     * @return the dataSource value.
     */
    public DataSource dataSource() {
        return this.dataSource;
    }

    /**
     * Set the dataSource property: Class representing data source used by the
     * detectors.
     * 
     * @param dataSource the dataSource value to set.
     * @return the ResponseMetaData object itself.
     */
    public ResponseMetaData withDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }
}