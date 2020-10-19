/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.batch.protocol.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for ComputeNodeFillType.
 */
public enum ComputeNodeFillType {
    /** Tasks should be assigned evenly across all Compute Nodes in the Pool. */
    SPREAD("spread"),

    /** As many Tasks as possible (taskSlotsPerNode) should be assigned to each Compute Node in the Pool before any Tasks are assigned to the next Compute Node in the Pool. */
    PACK("pack");

    /** The actual serialized value for a ComputeNodeFillType instance. */
    private String value;

    ComputeNodeFillType(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a ComputeNodeFillType instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed ComputeNodeFillType object, or null if unable to parse.
     */
    @JsonCreator
    public static ComputeNodeFillType fromString(String value) {
        ComputeNodeFillType[] items = ComputeNodeFillType.values();
        for (ComputeNodeFillType item : items) {
            if (item.toString().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
