/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * def
 */

package com.microsoft.azure.management.network.v2018_11_01.implementation;

import com.microsoft.azure.arm.resources.collection.implementation.GroupableResourcesCoreImpl;
import com.microsoft.azure.management.network.v2018_11_01.PublicIPAddresses;
import com.microsoft.azure.management.network.v2018_11_01.PublicIPAddress;
import rx.Observable;
import rx.Completable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import com.microsoft.azure.arm.resources.ResourceUtilsCore;
import com.microsoft.azure.arm.utils.RXMapper;
import rx.functions.Func1;
import com.microsoft.azure.PagedList;
import com.microsoft.azure.Page;

class PublicIPAddressesImpl extends GroupableResourcesCoreImpl<PublicIPAddress, PublicIPAddressImpl, PublicIPAddressInner, PublicIPAddressesInner, NetworkManager>  implements PublicIPAddresses {
    protected PublicIPAddressesImpl(NetworkManager manager) {
        super(manager.inner().publicIPAddresses(), manager);
    }

    @Override
    protected Observable<PublicIPAddressInner> getInnerAsync(String resourceGroupName, String name) {
        PublicIPAddressesInner client = this.inner();
        return client.getByResourceGroupAsync(resourceGroupName, name);
    }

    @Override
    protected Completable deleteInnerAsync(String resourceGroupName, String name) {
        PublicIPAddressesInner client = this.inner();
        return client.deleteAsync(resourceGroupName, name).toCompletable();
    }

    @Override
    public Observable<String> deleteByIdsAsync(Collection<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Observable.empty();
        }
        Collection<Observable<String>> observables = new ArrayList<>();
        for (String id : ids) {
            final String resourceGroupName = ResourceUtilsCore.groupFromResourceId(id);
            final String name = ResourceUtilsCore.nameFromResourceId(id);
            Observable<String> o = RXMapper.map(this.inner().deleteAsync(resourceGroupName, name), id);
            observables.add(o);
        }
        return Observable.mergeDelayError(observables);
    }

    @Override
    public Observable<String> deleteByIdsAsync(String...ids) {
        return this.deleteByIdsAsync(new ArrayList<String>(Arrays.asList(ids)));
    }

    @Override
    public void deleteByIds(Collection<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            this.deleteByIdsAsync(ids).toBlocking().last();
        }
    }

    @Override
    public void deleteByIds(String...ids) {
        this.deleteByIds(new ArrayList<String>(Arrays.asList(ids)));
    }

    @Override
    public PagedList<PublicIPAddress> listByResourceGroup(String resourceGroupName) {
        PublicIPAddressesInner client = this.inner();
        return this.wrapList(client.listByResourceGroup(resourceGroupName));
    }

    @Override
    public Observable<PublicIPAddress> listByResourceGroupAsync(String resourceGroupName) {
        PublicIPAddressesInner client = this.inner();
        return client.listByResourceGroupAsync(resourceGroupName)
        .flatMapIterable(new Func1<Page<PublicIPAddressInner>, Iterable<PublicIPAddressInner>>() {
            @Override
            public Iterable<PublicIPAddressInner> call(Page<PublicIPAddressInner> page) {
                return page.items();
            }
        })
        .map(new Func1<PublicIPAddressInner, PublicIPAddress>() {
            @Override
            public PublicIPAddress call(PublicIPAddressInner inner) {
                return wrapModel(inner);
            }
        });
    }

    @Override
    public PagedList<PublicIPAddress> list() {
        PublicIPAddressesInner client = this.inner();
        return this.wrapList(client.list());
    }

    @Override
    public Observable<PublicIPAddress> listAsync() {
        PublicIPAddressesInner client = this.inner();
        return client.listAsync()
        .flatMapIterable(new Func1<Page<PublicIPAddressInner>, Iterable<PublicIPAddressInner>>() {
            @Override
            public Iterable<PublicIPAddressInner> call(Page<PublicIPAddressInner> page) {
                return page.items();
            }
        })
        .map(new Func1<PublicIPAddressInner, PublicIPAddress>() {
            @Override
            public PublicIPAddress call(PublicIPAddressInner inner) {
                return wrapModel(inner);
            }
        });
    }

    @Override
    public PublicIPAddressImpl define(String name) {
        return wrapModel(name);
    }

    @Override
    public Observable<PublicIPAddress> listVirtualMachineScaleSetPublicIPAddressesAsync(final String resourceGroupName, final String virtualMachineScaleSetName) {
        PublicIPAddressesInner client = this.inner();
        return client.listVirtualMachineScaleSetPublicIPAddressesAsync(resourceGroupName, virtualMachineScaleSetName)
        .flatMapIterable(new Func1<Page<PublicIPAddressInner>, Iterable<PublicIPAddressInner>>() {
            @Override
            public Iterable<PublicIPAddressInner> call(Page<PublicIPAddressInner> page) {
                return page.items();
            }
        })
        .map(new Func1<PublicIPAddressInner, PublicIPAddress>() {
            @Override
            public PublicIPAddress call(PublicIPAddressInner inner) {
                return new PublicIPAddressImpl(inner.name(), inner, manager());
            }
        });
    }

    @Override
    public Observable<PublicIPAddress> listVirtualMachineScaleSetVMPublicIPAddressesAsync(final String resourceGroupName, final String virtualMachineScaleSetName, final String virtualmachineIndex, final String networkInterfaceName, final String ipConfigurationName) {
        PublicIPAddressesInner client = this.inner();
        return client.listVirtualMachineScaleSetVMPublicIPAddressesAsync(resourceGroupName, virtualMachineScaleSetName, virtualmachineIndex, networkInterfaceName, ipConfigurationName)
        .flatMapIterable(new Func1<Page<PublicIPAddressInner>, Iterable<PublicIPAddressInner>>() {
            @Override
            public Iterable<PublicIPAddressInner> call(Page<PublicIPAddressInner> page) {
                return page.items();
            }
        })
        .map(new Func1<PublicIPAddressInner, PublicIPAddress>() {
            @Override
            public PublicIPAddress call(PublicIPAddressInner inner) {
                return new PublicIPAddressImpl(inner.name(), inner, manager());
            }
        });
    }

    @Override
    public Observable<PublicIPAddress> getVirtualMachineScaleSetPublicIPAddressAsync(String resourceGroupName, String virtualMachineScaleSetName, String virtualmachineIndex, String networkInterfaceName, String ipConfigurationName, String publicIpAddressName) {
        PublicIPAddressesInner client = this.inner();
        return client.getVirtualMachineScaleSetPublicIPAddressAsync(resourceGroupName, virtualMachineScaleSetName, virtualmachineIndex, networkInterfaceName, ipConfigurationName, publicIpAddressName)
        .map(new Func1<PublicIPAddressInner, PublicIPAddress>() {
            @Override
            public PublicIPAddress call(PublicIPAddressInner inner) {
                return new PublicIPAddressImpl(inner.name(), inner, manager());
            }
        });
    }

    @Override
    protected PublicIPAddressImpl wrapModel(PublicIPAddressInner inner) {
        return  new PublicIPAddressImpl(inner.name(), inner, manager());
    }

    @Override
    protected PublicIPAddressImpl wrapModel(String name) {
        return new PublicIPAddressImpl(name, new PublicIPAddressInner(), this.manager());
    }

}
