// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.
// Changes may cause incorrect behavior and will be lost if the code is
// regenerated.

package com.azure.search.documents.indexes.implementation.models;

import com.azure.core.util.ExpandableStringEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Collection;

/** Defines values for CharFilterName. */
public final class CharFilterName extends ExpandableStringEnum<CharFilterName> {
    /** Static value html_strip for CharFilterName. */
    public static final CharFilterName HTML_STRIP = fromString("html_strip");

    /**
     * Creates or finds a CharFilterName from its string representation.
     *
     * @param name a name to look for.
     * @return the corresponding CharFilterName.
     */
    @JsonCreator
    public static CharFilterName fromString(String name) {
        return fromString(name, CharFilterName.class);
    }

    /** @return known CharFilterName values. */
    public static Collection<CharFilterName> values() {
        return values(CharFilterName.class);
    }
}
