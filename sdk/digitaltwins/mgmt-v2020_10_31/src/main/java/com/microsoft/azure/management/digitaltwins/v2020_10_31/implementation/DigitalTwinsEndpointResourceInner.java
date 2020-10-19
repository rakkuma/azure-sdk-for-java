/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.digitaltwins.v2020_10_31.implementation;

import com.microsoft.azure.management.digitaltwins.v2020_10_31.DigitalTwinsEndpointResourceProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.management.digitaltwins.v2020_10_31.ExternalResource;

/**
 * DigitalTwinsInstance endpoint resource.
 */
public class DigitalTwinsEndpointResourceInner extends ExternalResource {
    /**
     * DigitalTwinsInstance endpoint resource properties.
     */
    @JsonProperty(value = "properties", required = true)
    private DigitalTwinsEndpointResourceProperties properties;

    /**
     * Get digitalTwinsInstance endpoint resource properties.
     *
     * @return the properties value
     */
    public DigitalTwinsEndpointResourceProperties properties() {
        return this.properties;
    }

    /**
     * Set digitalTwinsInstance endpoint resource properties.
     *
     * @param properties the properties value to set
     * @return the DigitalTwinsEndpointResourceInner object itself.
     */
    public DigitalTwinsEndpointResourceInner withProperties(DigitalTwinsEndpointResourceProperties properties) {
        this.properties = properties;
        return this;
    }

}
