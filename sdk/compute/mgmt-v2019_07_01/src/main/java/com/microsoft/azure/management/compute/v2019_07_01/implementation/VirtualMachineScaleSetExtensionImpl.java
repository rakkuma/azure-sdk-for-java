/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.compute.v2019_07_01.implementation;

import com.microsoft.azure.management.compute.v2019_07_01.VirtualMachineScaleSetExtension;
import com.microsoft.azure.arm.model.implementation.CreatableUpdatableImpl;
import rx.Observable;
import com.microsoft.azure.management.compute.v2019_07_01.VirtualMachineScaleSetExtensionUpdate;
import java.util.List;
import rx.functions.Func1;

class VirtualMachineScaleSetExtensionImpl extends CreatableUpdatableImpl<VirtualMachineScaleSetExtension, VirtualMachineScaleSetExtensionInner, VirtualMachineScaleSetExtensionImpl> implements VirtualMachineScaleSetExtension, VirtualMachineScaleSetExtension.Definition, VirtualMachineScaleSetExtension.Update {
    private final ComputeManager manager;
    private String resourceGroupName;
    private String vmScaleSetName;
    private String vmssExtensionName;
    private VirtualMachineScaleSetExtensionUpdate updateParameter;

    VirtualMachineScaleSetExtensionImpl(String name, ComputeManager manager) {
        super(name, new VirtualMachineScaleSetExtensionInner());
        this.manager = manager;
        // Set resource name
        this.vmssExtensionName = name;
        //
        this.updateParameter = new VirtualMachineScaleSetExtensionUpdate();
    }

    VirtualMachineScaleSetExtensionImpl(VirtualMachineScaleSetExtensionInner inner, ComputeManager manager) {
        super(inner.name(), inner);
        this.manager = manager;
        // Set resource name
        this.vmssExtensionName = inner.name();
        // set resource ancestor and positional variables
        this.resourceGroupName = IdParsingUtils.getValueFromIdByName(inner.id(), "resourceGroups");
        this.vmScaleSetName = IdParsingUtils.getValueFromIdByName(inner.id(), "virtualMachineScaleSets");
        this.vmssExtensionName = IdParsingUtils.getValueFromIdByName(inner.id(), "extensions");
        //
        this.updateParameter = new VirtualMachineScaleSetExtensionUpdate();
    }

    @Override
    public ComputeManager manager() {
        return this.manager;
    }

    @Override
    public Observable<VirtualMachineScaleSetExtension> createResourceAsync() {
        VirtualMachineScaleSetExtensionsInner client = this.manager().inner().virtualMachineScaleSetExtensions();
        return client.createOrUpdateAsync(this.resourceGroupName, this.vmScaleSetName, this.vmssExtensionName, this.inner())
            .map(new Func1<VirtualMachineScaleSetExtensionInner, VirtualMachineScaleSetExtensionInner>() {
               @Override
               public VirtualMachineScaleSetExtensionInner call(VirtualMachineScaleSetExtensionInner resource) {
                   resetCreateUpdateParameters();
                   return resource;
               }
            })
            .map(innerToFluentMap(this));
    }

    @Override
    public Observable<VirtualMachineScaleSetExtension> updateResourceAsync() {
        VirtualMachineScaleSetExtensionsInner client = this.manager().inner().virtualMachineScaleSetExtensions();
        return client.updateAsync(this.resourceGroupName, this.vmScaleSetName, this.vmssExtensionName, this.updateParameter)
            .map(new Func1<VirtualMachineScaleSetExtensionInner, VirtualMachineScaleSetExtensionInner>() {
               @Override
               public VirtualMachineScaleSetExtensionInner call(VirtualMachineScaleSetExtensionInner resource) {
                   resetCreateUpdateParameters();
                   return resource;
               }
            })
            .map(innerToFluentMap(this));
    }

    @Override
    protected Observable<VirtualMachineScaleSetExtensionInner> getInnerAsync() {
        VirtualMachineScaleSetExtensionsInner client = this.manager().inner().virtualMachineScaleSetExtensions();
        return client.getAsync(this.resourceGroupName, this.vmScaleSetName, this.vmssExtensionName);
    }

    @Override
    public boolean isInCreateMode() {
        return this.inner().id() == null;
    }

    private void resetCreateUpdateParameters() {
        this.updateParameter = new VirtualMachineScaleSetExtensionUpdate();
    }

    @Override
    public Boolean autoUpgradeMinorVersion() {
        return this.inner().autoUpgradeMinorVersion();
    }

    @Override
    public String forceUpdateTag() {
        return this.inner().forceUpdateTag();
    }

    @Override
    public String id() {
        return this.inner().id();
    }

    @Override
    public String name() {
        return this.inner().name();
    }

    @Override
    public Object protectedSettings() {
        return this.inner().protectedSettings();
    }

    @Override
    public List<String> provisionAfterExtensions() {
        return this.inner().provisionAfterExtensions();
    }

    @Override
    public String provisioningState() {
        return this.inner().provisioningState();
    }

    @Override
    public String publisher() {
        return this.inner().publisher();
    }

    @Override
    public Object settings() {
        return this.inner().settings();
    }

    @Override
    public String type() {
        return this.inner().type();
    }

    @Override
    public String type1() {
        return this.inner().type1();
    }

    @Override
    public String typeHandlerVersion() {
        return this.inner().typeHandlerVersion();
    }

    @Override
    public VirtualMachineScaleSetExtensionImpl withExistingVirtualMachineScaleSet(String resourceGroupName, String vmScaleSetName) {
        this.resourceGroupName = resourceGroupName;
        this.vmScaleSetName = vmScaleSetName;
        return this;
    }

    @Override
    public VirtualMachineScaleSetExtensionImpl withName(String name) {
        this.inner().withName(name);
        return this;
    }

    @Override
    public VirtualMachineScaleSetExtensionImpl withAutoUpgradeMinorVersion(Boolean autoUpgradeMinorVersion) {
        if (isInCreateMode()) {
            this.inner().withAutoUpgradeMinorVersion(autoUpgradeMinorVersion);
        } else {
            this.updateParameter.withAutoUpgradeMinorVersion(autoUpgradeMinorVersion);
        }
        return this;
    }

    @Override
    public VirtualMachineScaleSetExtensionImpl withForceUpdateTag(String forceUpdateTag) {
        if (isInCreateMode()) {
            this.inner().withForceUpdateTag(forceUpdateTag);
        } else {
            this.updateParameter.withForceUpdateTag(forceUpdateTag);
        }
        return this;
    }

    @Override
    public VirtualMachineScaleSetExtensionImpl withProtectedSettings(Object protectedSettings) {
        if (isInCreateMode()) {
            this.inner().withProtectedSettings(protectedSettings);
        } else {
            this.updateParameter.withProtectedSettings(protectedSettings);
        }
        return this;
    }

    @Override
    public VirtualMachineScaleSetExtensionImpl withProvisionAfterExtensions(List<String> provisionAfterExtensions) {
        if (isInCreateMode()) {
            this.inner().withProvisionAfterExtensions(provisionAfterExtensions);
        } else {
            this.updateParameter.withProvisionAfterExtensions(provisionAfterExtensions);
        }
        return this;
    }

    @Override
    public VirtualMachineScaleSetExtensionImpl withPublisher(String publisher) {
        if (isInCreateMode()) {
            this.inner().withPublisher(publisher);
        } else {
            this.updateParameter.withPublisher(publisher);
        }
        return this;
    }

    @Override
    public VirtualMachineScaleSetExtensionImpl withSettings(Object settings) {
        if (isInCreateMode()) {
            this.inner().withSettings(settings);
        } else {
            this.updateParameter.withSettings(settings);
        }
        return this;
    }

    @Override
    public VirtualMachineScaleSetExtensionImpl withType1(String type1) {
        if (isInCreateMode()) {
            this.inner().withType1(type1);
        } else {
            this.updateParameter.withType1(type1);
        }
        return this;
    }

    @Override
    public VirtualMachineScaleSetExtensionImpl withTypeHandlerVersion(String typeHandlerVersion) {
        if (isInCreateMode()) {
            this.inner().withTypeHandlerVersion(typeHandlerVersion);
        } else {
            this.updateParameter.withTypeHandlerVersion(typeHandlerVersion);
        }
        return this;
    }

}
