/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.management.resources.collection;

import com.microsoft.azure.CloudException;
import com.microsoft.azure.ListOperationCallback;
import com.microsoft.azure.management.resources.models.dto.toplevel.Deployment;
import com.microsoft.azure.management.resources.models.dto.DeploymentExtendedFilter;
import com.microsoft.azure.management.resources.models.dto.DeploymentValidateResult;
import com.microsoft.azure.management.resources.models.dto.PageImpl;
import com.microsoft.azure.management.resources.models.dto.toplevel.DeploymentExtended;
import com.microsoft.azure.PagedList;
import com.microsoft.rest.ServiceCall;
import com.microsoft.rest.ServiceCallback;
import com.microsoft.rest.ServiceResponse;
import java.io.IOException;

/**
 * An instance of this class provides access to all the operations defined
 * in Deployments.
 */
public interface Deployments {
    /**
     * Begin deleting deployment.To determine whether the operation has finished processing the request, call GetLongRunningOperationStatus.
     *
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param deploymentName The name of the deployment to be deleted.
     * @throws CloudException exception thrown from REST call
     * @throws IOException exception thrown from serialization/deserialization
     * @throws IllegalArgumentException exception thrown from invalid parameters
     * @throws InterruptedException exception thrown when long running operation is interrupted
     * @return the {@link ServiceResponse} object if successful.
     */
    ServiceResponse<Void> delete(String resourceGroupName, String deploymentName) throws CloudException, IOException, IllegalArgumentException, InterruptedException;

    /**
     * Begin deleting deployment.To determine whether the operation has finished processing the request, call GetLongRunningOperationStatus.
     *
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param deploymentName The name of the deployment to be deleted.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if callback is null
     * @return the {@link ServiceCall} object
     */
    ServiceCall deleteAsync(String resourceGroupName, String deploymentName, final ServiceCallback<Void> serviceCallback) throws IllegalArgumentException;

    /**
     * Begin deleting deployment.To determine whether the operation has finished processing the request, call GetLongRunningOperationStatus.
     *
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param deploymentName The name of the deployment to be deleted.
     * @throws CloudException exception thrown from REST call
     * @throws IOException exception thrown from serialization/deserialization
     * @throws IllegalArgumentException exception thrown from invalid parameters
     * @return the {@link ServiceResponse} object if successful.
     */
    ServiceResponse<Void> beginDelete(String resourceGroupName, String deploymentName) throws CloudException, IOException, IllegalArgumentException;

    /**
     * Begin deleting deployment.To determine whether the operation has finished processing the request, call GetLongRunningOperationStatus.
     *
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param deploymentName The name of the deployment to be deleted.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if callback is null
     * @return the {@link ServiceCall} object
     */
    ServiceCall beginDeleteAsync(String resourceGroupName, String deploymentName, final ServiceCallback<Void> serviceCallback) throws IllegalArgumentException;

    /**
     * Checks whether deployment exists.
     *
     * @param resourceGroupName The name of the resource group to check. The name is case insensitive.
     * @param deploymentName The name of the deployment.
     * @throws CloudException exception thrown from REST call
     * @throws IOException exception thrown from serialization/deserialization
     * @throws IllegalArgumentException exception thrown from invalid parameters
     * @return the boolean object wrapped in {@link ServiceResponse} if successful.
     */
    ServiceResponse<Boolean> checkExistence(String resourceGroupName, String deploymentName) throws CloudException, IOException, IllegalArgumentException;

    /**
     * Checks whether deployment exists.
     *
     * @param resourceGroupName The name of the resource group to check. The name is case insensitive.
     * @param deploymentName The name of the deployment.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if callback is null
     * @return the {@link ServiceCall} object
     */
    ServiceCall checkExistenceAsync(String resourceGroupName, String deploymentName, final ServiceCallback<Boolean> serviceCallback) throws IllegalArgumentException;

    /**
     * Create a named template deployment using a template.
     *
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param deploymentName The name of the deployment.
     * @param parameters Additional parameters supplied to the operation.
     * @throws CloudException exception thrown from REST call
     * @throws IOException exception thrown from serialization/deserialization
     * @throws IllegalArgumentException exception thrown from invalid parameters
     * @throws InterruptedException exception thrown when long running operation is interrupted
     * @return the DeploymentExtended object wrapped in {@link ServiceResponse} if successful.
     */
    ServiceResponse<DeploymentExtended> createOrUpdate(String resourceGroupName, String deploymentName, Deployment parameters) throws CloudException, IOException, IllegalArgumentException, InterruptedException;

    /**
     * Create a named template deployment using a template.
     *
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param deploymentName The name of the deployment.
     * @param parameters Additional parameters supplied to the operation.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if callback is null
     * @return the {@link ServiceCall} object
     */
    ServiceCall createOrUpdateAsync(String resourceGroupName, String deploymentName, Deployment parameters, final ServiceCallback<DeploymentExtended> serviceCallback) throws IllegalArgumentException;

    /**
     * Create a named template deployment using a template.
     *
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param deploymentName The name of the deployment.
     * @param parameters Additional parameters supplied to the operation.
     * @throws CloudException exception thrown from REST call
     * @throws IOException exception thrown from serialization/deserialization
     * @throws IllegalArgumentException exception thrown from invalid parameters
     * @return the DeploymentExtended object wrapped in {@link ServiceResponse} if successful.
     */
    ServiceResponse<DeploymentExtended> beginCreateOrUpdate(String resourceGroupName, String deploymentName, Deployment parameters) throws CloudException, IOException, IllegalArgumentException;

    /**
     * Create a named template deployment using a template.
     *
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param deploymentName The name of the deployment.
     * @param parameters Additional parameters supplied to the operation.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if callback is null
     * @return the {@link ServiceCall} object
     */
    ServiceCall beginCreateOrUpdateAsync(String resourceGroupName, String deploymentName, Deployment parameters, final ServiceCallback<DeploymentExtended> serviceCallback) throws IllegalArgumentException;

    /**
     * Get a deployment.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param deploymentName The name of the deployment.
     * @throws CloudException exception thrown from REST call
     * @throws IOException exception thrown from serialization/deserialization
     * @throws IllegalArgumentException exception thrown from invalid parameters
     * @return the DeploymentExtended object wrapped in {@link ServiceResponse} if successful.
     */
    ServiceResponse<DeploymentExtended> get(String resourceGroupName, String deploymentName) throws CloudException, IOException, IllegalArgumentException;

    /**
     * Get a deployment.
     *
     * @param resourceGroupName The name of the resource group to get. The name is case insensitive.
     * @param deploymentName The name of the deployment.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if callback is null
     * @return the {@link ServiceCall} object
     */
    ServiceCall getAsync(String resourceGroupName, String deploymentName, final ServiceCallback<DeploymentExtended> serviceCallback) throws IllegalArgumentException;

    /**
     * Cancel a currently running template deployment.
     *
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param deploymentName The name of the deployment.
     * @throws CloudException exception thrown from REST call
     * @throws IOException exception thrown from serialization/deserialization
     * @throws IllegalArgumentException exception thrown from invalid parameters
     * @return the {@link ServiceResponse} object if successful.
     */
    ServiceResponse<Void> cancel(String resourceGroupName, String deploymentName) throws CloudException, IOException, IllegalArgumentException;

    /**
     * Cancel a currently running template deployment.
     *
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param deploymentName The name of the deployment.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if callback is null
     * @return the {@link ServiceCall} object
     */
    ServiceCall cancelAsync(String resourceGroupName, String deploymentName, final ServiceCallback<Void> serviceCallback) throws IllegalArgumentException;

    /**
     * Validate a deployment template.
     *
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param deploymentName The name of the deployment.
     * @param parameters Deployment to validate.
     * @throws CloudException exception thrown from REST call
     * @throws IOException exception thrown from serialization/deserialization
     * @throws IllegalArgumentException exception thrown from invalid parameters
     * @return the DeploymentValidateResult object wrapped in {@link ServiceResponse} if successful.
     */
    ServiceResponse<DeploymentValidateResult> validate(String resourceGroupName, String deploymentName, Deployment parameters) throws CloudException, IOException, IllegalArgumentException;

    /**
     * Validate a deployment template.
     *
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param deploymentName The name of the deployment.
     * @param parameters Deployment to validate.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if callback is null
     * @return the {@link ServiceCall} object
     */
    ServiceCall validateAsync(String resourceGroupName, String deploymentName, Deployment parameters, final ServiceCallback<DeploymentValidateResult> serviceCallback) throws IllegalArgumentException;

    /**
     * Get a list of deployments.
     *
     * @param resourceGroupName The name of the resource group to filter by. The name is case insensitive.
     * @throws CloudException exception thrown from REST call
     * @throws IOException exception thrown from serialization/deserialization
     * @throws IllegalArgumentException exception thrown from invalid parameters
     * @return the List&lt;DeploymentExtended&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    ServiceResponse<PagedList<DeploymentExtended>> list(final String resourceGroupName) throws CloudException, IOException, IllegalArgumentException;

    /**
     * Get a list of deployments.
     *
     * @param resourceGroupName The name of the resource group to filter by. The name is case insensitive.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if callback is null
     * @return the {@link ServiceCall} object
     */
    ServiceCall listAsync(final String resourceGroupName, final ListOperationCallback<DeploymentExtended> serviceCallback) throws IllegalArgumentException;
    /**
     * Get a list of deployments.
     *
     * @param resourceGroupName The name of the resource group to filter by. The name is case insensitive.
     * @param filter The filter to apply on the operation.
     * @param top Query parameters. If null is passed returns all deployments.
     * @throws CloudException exception thrown from REST call
     * @throws IOException exception thrown from serialization/deserialization
     * @throws IllegalArgumentException exception thrown from invalid parameters
     * @return the List&lt;DeploymentExtended&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    ServiceResponse<PagedList<DeploymentExtended>> list(final String resourceGroupName, final DeploymentExtendedFilter filter, final Integer top) throws CloudException, IOException, IllegalArgumentException;

    /**
     * Get a list of deployments.
     *
     * @param resourceGroupName The name of the resource group to filter by. The name is case insensitive.
     * @param filter The filter to apply on the operation.
     * @param top Query parameters. If null is passed returns all deployments.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if callback is null
     * @return the {@link ServiceCall} object
     */
    ServiceCall listAsync(final String resourceGroupName, final DeploymentExtendedFilter filter, final Integer top, final ListOperationCallback<DeploymentExtended> serviceCallback) throws IllegalArgumentException;

    /**
     * Get a list of deployments.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws CloudException exception thrown from REST call
     * @throws IOException exception thrown from serialization/deserialization
     * @throws IllegalArgumentException exception thrown from invalid parameters
     * @return the List&lt;DeploymentExtended&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    ServiceResponse<PageImpl<DeploymentExtended>> listNext(final String nextPageLink) throws CloudException, IOException, IllegalArgumentException;

    /**
     * Get a list of deployments.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceCall the ServiceCall object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if callback is null
     * @return the {@link ServiceCall} object
     */
    ServiceCall listNextAsync(final String nextPageLink, final ServiceCall serviceCall, final ListOperationCallback<DeploymentExtended> serviceCallback) throws IllegalArgumentException;

}
