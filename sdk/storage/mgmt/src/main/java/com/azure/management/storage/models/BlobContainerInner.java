// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.management.storage.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.annotation.JsonFlatten;
import com.azure.management.storage.AzureEntityResource;
import com.azure.management.storage.ImmutabilityPolicyProperties;
import com.azure.management.storage.LeaseDuration;
import com.azure.management.storage.LeaseState;
import com.azure.management.storage.LeaseStatus;
import com.azure.management.storage.LegalHoldProperties;
import com.azure.management.storage.PublicAccess;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * The BlobContainer model.
 */
@JsonFlatten
@Fluent
public class BlobContainerInner extends AzureEntityResource {
    /*
     * Specifies whether data in the container may be accessed publicly and the
     * level of access.
     */
    @JsonProperty(value = "properties.publicAccess")
    private PublicAccess publicAccess;

    /*
     * Returns the date and time the container was last modified.
     */
    @JsonProperty(value = "properties.lastModifiedTime", access = JsonProperty.Access.WRITE_ONLY)
    private OffsetDateTime lastModifiedTime;

    /*
     * The lease status of the container.
     */
    @JsonProperty(value = "properties.leaseStatus", access = JsonProperty.Access.WRITE_ONLY)
    private LeaseStatus leaseStatus;

    /*
     * Lease state of the container.
     */
    @JsonProperty(value = "properties.leaseState", access = JsonProperty.Access.WRITE_ONLY)
    private LeaseState leaseState;

    /*
     * Specifies whether the lease on a container is of infinite or fixed
     * duration, only when the container is leased.
     */
    @JsonProperty(value = "properties.leaseDuration", access = JsonProperty.Access.WRITE_ONLY)
    private LeaseDuration leaseDuration;

    /*
     * A name-value pair to associate with the container as metadata.
     */
    @JsonProperty(value = "properties.metadata")
    private Map<String, String> metadata;

    /*
     * The ImmutabilityPolicy property of the container.
     */
    @JsonProperty(value = "properties.immutabilityPolicy", access = JsonProperty.Access.WRITE_ONLY)
    private ImmutabilityPolicyProperties immutabilityPolicy;

    /*
     * The LegalHold property of the container.
     */
    @JsonProperty(value = "properties.legalHold", access = JsonProperty.Access.WRITE_ONLY)
    private LegalHoldProperties legalHold;

    /*
     * The hasLegalHold public property is set to true by SRP if there are at
     * least one existing tag. The hasLegalHold public property is set to false
     * by SRP if all existing legal hold tags are cleared out. There can be a
     * maximum of 1000 blob containers with hasLegalHold=true for a given
     * account.
     */
    @JsonProperty(value = "properties.hasLegalHold", access = JsonProperty.Access.WRITE_ONLY)
    private Boolean hasLegalHold;

    /*
     * The hasImmutabilityPolicy public property is set to true by SRP if
     * ImmutabilityPolicy has been created for this container. The
     * hasImmutabilityPolicy public property is set to false by SRP if
     * ImmutabilityPolicy has not been created for this container.
     */
    @JsonProperty(value = "properties.hasImmutabilityPolicy", access = JsonProperty.Access.WRITE_ONLY)
    private Boolean hasImmutabilityPolicy;

    /**
     * Get the publicAccess property: Specifies whether data in the container
     * may be accessed publicly and the level of access.
     * 
     * @return the publicAccess value.
     */
    public PublicAccess publicAccess() {
        return this.publicAccess;
    }

    /**
     * Set the publicAccess property: Specifies whether data in the container
     * may be accessed publicly and the level of access.
     * 
     * @param publicAccess the publicAccess value to set.
     * @return the BlobContainerInner object itself.
     */
    public BlobContainerInner withPublicAccess(PublicAccess publicAccess) {
        this.publicAccess = publicAccess;
        return this;
    }

    /**
     * Get the lastModifiedTime property: Returns the date and time the
     * container was last modified.
     * 
     * @return the lastModifiedTime value.
     */
    public OffsetDateTime lastModifiedTime() {
        return this.lastModifiedTime;
    }

    /**
     * Get the leaseStatus property: The lease status of the container.
     * 
     * @return the leaseStatus value.
     */
    public LeaseStatus leaseStatus() {
        return this.leaseStatus;
    }

    /**
     * Get the leaseState property: Lease state of the container.
     * 
     * @return the leaseState value.
     */
    public LeaseState leaseState() {
        return this.leaseState;
    }

    /**
     * Get the leaseDuration property: Specifies whether the lease on a
     * container is of infinite or fixed duration, only when the container is
     * leased.
     * 
     * @return the leaseDuration value.
     */
    public LeaseDuration leaseDuration() {
        return this.leaseDuration;
    }

    /**
     * Get the metadata property: A name-value pair to associate with the
     * container as metadata.
     * 
     * @return the metadata value.
     */
    public Map<String, String> metadata() {
        return this.metadata;
    }

    /**
     * Set the metadata property: A name-value pair to associate with the
     * container as metadata.
     * 
     * @param metadata the metadata value to set.
     * @return the BlobContainerInner object itself.
     */
    public BlobContainerInner withMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Get the immutabilityPolicy property: The ImmutabilityPolicy property of
     * the container.
     * 
     * @return the immutabilityPolicy value.
     */
    public ImmutabilityPolicyProperties immutabilityPolicy() {
        return this.immutabilityPolicy;
    }

    /**
     * Get the legalHold property: The LegalHold property of the container.
     * 
     * @return the legalHold value.
     */
    public LegalHoldProperties legalHold() {
        return this.legalHold;
    }

    /**
     * Get the hasLegalHold property: The hasLegalHold public property is set
     * to true by SRP if there are at least one existing tag. The hasLegalHold
     * public property is set to false by SRP if all existing legal hold tags
     * are cleared out. There can be a maximum of 1000 blob containers with
     * hasLegalHold=true for a given account.
     * 
     * @return the hasLegalHold value.
     */
    public Boolean hasLegalHold() {
        return this.hasLegalHold;
    }

    /**
     * Get the hasImmutabilityPolicy property: The hasImmutabilityPolicy public
     * property is set to true by SRP if ImmutabilityPolicy has been created
     * for this container. The hasImmutabilityPolicy public property is set to
     * false by SRP if ImmutabilityPolicy has not been created for this
     * container.
     * 
     * @return the hasImmutabilityPolicy value.
     */
    public Boolean hasImmutabilityPolicy() {
        return this.hasImmutabilityPolicy;
    }
}