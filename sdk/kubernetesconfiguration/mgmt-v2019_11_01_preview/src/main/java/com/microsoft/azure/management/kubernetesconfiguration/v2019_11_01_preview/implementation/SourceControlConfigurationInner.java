/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.kubernetesconfiguration.v2019_11_01_preview.implementation;

import com.microsoft.azure.management.kubernetesconfiguration.v2019_11_01_preview.OperatorType;
import com.microsoft.azure.management.kubernetesconfiguration.v2019_11_01_preview.OperatorScope;
import com.microsoft.azure.management.kubernetesconfiguration.v2019_11_01_preview.EnableHelmOperator;
import com.microsoft.azure.management.kubernetesconfiguration.v2019_11_01_preview.HelmOperatorProperties;
import com.microsoft.azure.management.kubernetesconfiguration.v2019_11_01_preview.ProvisioningState;
import com.microsoft.azure.management.kubernetesconfiguration.v2019_11_01_preview.ComplianceStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.rest.serializer.JsonFlatten;
import com.microsoft.azure.ProxyResource;

/**
 * The SourceControl Configuration object.
 */
@JsonFlatten
public class SourceControlConfigurationInner extends ProxyResource {
    /**
     * Url of the SourceControl Repository.
     */
    @JsonProperty(value = "properties.repositoryUrl")
    private String repositoryUrl;

    /**
     * The namespace to which this operator is installed to. Maximum of 253
     * lower case alphanumeric characters, hyphen and period only.
     */
    @JsonProperty(value = "properties.operatorNamespace")
    private String operatorNamespace;

    /**
     * Instance name of the operator - identifying the specific configuration.
     */
    @JsonProperty(value = "properties.operatorInstanceName")
    private String operatorInstanceName;

    /**
     * Type of the operator. Possible values include: 'Flux'.
     */
    @JsonProperty(value = "properties.operatorType")
    private OperatorType operatorType;

    /**
     * Any Parameters for the Operator instance in string format.
     */
    @JsonProperty(value = "properties.operatorParams")
    private String operatorParams;

    /**
     * Scope at which the operator will be installed. Possible values include:
     * 'cluster', 'namespace'.
     */
    @JsonProperty(value = "properties.operatorScope")
    private OperatorScope operatorScope;

    /**
     * Public Key associated with this SourceControl configuration (either
     * generated within the cluster or provided by the user).
     */
    @JsonProperty(value = "properties.repositoryPublicKey", access = JsonProperty.Access.WRITE_ONLY)
    private String repositoryPublicKey;

    /**
     * Option to enable Helm Operator for this git configuration. Possible
     * values include: 'true', 'false'.
     */
    @JsonProperty(value = "properties.enableHelmOperator")
    private EnableHelmOperator enableHelmOperator;

    /**
     * Properties for Helm operator.
     */
    @JsonProperty(value = "properties.helmOperatorProperties")
    private HelmOperatorProperties helmOperatorProperties;

    /**
     * The provisioning state of the resource provider. Possible values
     * include: 'Accepted', 'Deleting', 'Running', 'Succeeded', 'Failed'.
     */
    @JsonProperty(value = "properties.provisioningState", access = JsonProperty.Access.WRITE_ONLY)
    private ProvisioningState provisioningState;

    /**
     * Compliance Status of the Configuration.
     */
    @JsonProperty(value = "properties.complianceStatus", access = JsonProperty.Access.WRITE_ONLY)
    private ComplianceStatus complianceStatus;

    /**
     * Get url of the SourceControl Repository.
     *
     * @return the repositoryUrl value
     */
    public String repositoryUrl() {
        return this.repositoryUrl;
    }

    /**
     * Set url of the SourceControl Repository.
     *
     * @param repositoryUrl the repositoryUrl value to set
     * @return the SourceControlConfigurationInner object itself.
     */
    public SourceControlConfigurationInner withRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
        return this;
    }

    /**
     * Get the namespace to which this operator is installed to. Maximum of 253 lower case alphanumeric characters, hyphen and period only.
     *
     * @return the operatorNamespace value
     */
    public String operatorNamespace() {
        return this.operatorNamespace;
    }

    /**
     * Set the namespace to which this operator is installed to. Maximum of 253 lower case alphanumeric characters, hyphen and period only.
     *
     * @param operatorNamespace the operatorNamespace value to set
     * @return the SourceControlConfigurationInner object itself.
     */
    public SourceControlConfigurationInner withOperatorNamespace(String operatorNamespace) {
        this.operatorNamespace = operatorNamespace;
        return this;
    }

    /**
     * Get instance name of the operator - identifying the specific configuration.
     *
     * @return the operatorInstanceName value
     */
    public String operatorInstanceName() {
        return this.operatorInstanceName;
    }

    /**
     * Set instance name of the operator - identifying the specific configuration.
     *
     * @param operatorInstanceName the operatorInstanceName value to set
     * @return the SourceControlConfigurationInner object itself.
     */
    public SourceControlConfigurationInner withOperatorInstanceName(String operatorInstanceName) {
        this.operatorInstanceName = operatorInstanceName;
        return this;
    }

    /**
     * Get type of the operator. Possible values include: 'Flux'.
     *
     * @return the operatorType value
     */
    public OperatorType operatorType() {
        return this.operatorType;
    }

    /**
     * Set type of the operator. Possible values include: 'Flux'.
     *
     * @param operatorType the operatorType value to set
     * @return the SourceControlConfigurationInner object itself.
     */
    public SourceControlConfigurationInner withOperatorType(OperatorType operatorType) {
        this.operatorType = operatorType;
        return this;
    }

    /**
     * Get any Parameters for the Operator instance in string format.
     *
     * @return the operatorParams value
     */
    public String operatorParams() {
        return this.operatorParams;
    }

    /**
     * Set any Parameters for the Operator instance in string format.
     *
     * @param operatorParams the operatorParams value to set
     * @return the SourceControlConfigurationInner object itself.
     */
    public SourceControlConfigurationInner withOperatorParams(String operatorParams) {
        this.operatorParams = operatorParams;
        return this;
    }

    /**
     * Get scope at which the operator will be installed. Possible values include: 'cluster', 'namespace'.
     *
     * @return the operatorScope value
     */
    public OperatorScope operatorScope() {
        return this.operatorScope;
    }

    /**
     * Set scope at which the operator will be installed. Possible values include: 'cluster', 'namespace'.
     *
     * @param operatorScope the operatorScope value to set
     * @return the SourceControlConfigurationInner object itself.
     */
    public SourceControlConfigurationInner withOperatorScope(OperatorScope operatorScope) {
        this.operatorScope = operatorScope;
        return this;
    }

    /**
     * Get public Key associated with this SourceControl configuration (either generated within the cluster or provided by the user).
     *
     * @return the repositoryPublicKey value
     */
    public String repositoryPublicKey() {
        return this.repositoryPublicKey;
    }

    /**
     * Get option to enable Helm Operator for this git configuration. Possible values include: 'true', 'false'.
     *
     * @return the enableHelmOperator value
     */
    public EnableHelmOperator enableHelmOperator() {
        return this.enableHelmOperator;
    }

    /**
     * Set option to enable Helm Operator for this git configuration. Possible values include: 'true', 'false'.
     *
     * @param enableHelmOperator the enableHelmOperator value to set
     * @return the SourceControlConfigurationInner object itself.
     */
    public SourceControlConfigurationInner withEnableHelmOperator(EnableHelmOperator enableHelmOperator) {
        this.enableHelmOperator = enableHelmOperator;
        return this;
    }

    /**
     * Get properties for Helm operator.
     *
     * @return the helmOperatorProperties value
     */
    public HelmOperatorProperties helmOperatorProperties() {
        return this.helmOperatorProperties;
    }

    /**
     * Set properties for Helm operator.
     *
     * @param helmOperatorProperties the helmOperatorProperties value to set
     * @return the SourceControlConfigurationInner object itself.
     */
    public SourceControlConfigurationInner withHelmOperatorProperties(HelmOperatorProperties helmOperatorProperties) {
        this.helmOperatorProperties = helmOperatorProperties;
        return this;
    }

    /**
     * Get the provisioning state of the resource provider. Possible values include: 'Accepted', 'Deleting', 'Running', 'Succeeded', 'Failed'.
     *
     * @return the provisioningState value
     */
    public ProvisioningState provisioningState() {
        return this.provisioningState;
    }

    /**
     * Get compliance Status of the Configuration.
     *
     * @return the complianceStatus value
     */
    public ComplianceStatus complianceStatus() {
        return this.complianceStatus;
    }

}
