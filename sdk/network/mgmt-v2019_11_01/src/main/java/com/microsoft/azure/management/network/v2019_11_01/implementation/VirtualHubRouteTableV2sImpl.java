/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 *
 */

package com.microsoft.azure.management.network.v2019_11_01.implementation;

import com.microsoft.azure.arm.model.implementation.WrapperImpl;
import com.microsoft.azure.management.network.v2019_11_01.VirtualHubRouteTableV2s;
import rx.Completable;
import rx.Observable;
import rx.functions.Func1;
import com.microsoft.azure.Page;
import com.microsoft.azure.management.network.v2019_11_01.VirtualHubRouteTableV2;

class VirtualHubRouteTableV2sImpl extends WrapperImpl<VirtualHubRouteTableV2sInner> implements VirtualHubRouteTableV2s {
    private final NetworkManager manager;

    VirtualHubRouteTableV2sImpl(NetworkManager manager) {
        super(manager.inner().virtualHubRouteTableV2s());
        this.manager = manager;
    }

    public NetworkManager manager() {
        return this.manager;
    }

    @Override
    public VirtualHubRouteTableV2Impl define(String name) {
        return wrapModel(name);
    }

    private VirtualHubRouteTableV2Impl wrapModel(VirtualHubRouteTableV2Inner inner) {
        return  new VirtualHubRouteTableV2Impl(inner, manager());
    }

    private VirtualHubRouteTableV2Impl wrapModel(String name) {
        return new VirtualHubRouteTableV2Impl(name, this.manager());
    }

    @Override
    public Observable<VirtualHubRouteTableV2> listAsync(final String resourceGroupName, final String virtualHubName) {
        VirtualHubRouteTableV2sInner client = this.inner();
        return client.listAsync(resourceGroupName, virtualHubName)
        .flatMapIterable(new Func1<Page<VirtualHubRouteTableV2Inner>, Iterable<VirtualHubRouteTableV2Inner>>() {
            @Override
            public Iterable<VirtualHubRouteTableV2Inner> call(Page<VirtualHubRouteTableV2Inner> page) {
                return page.items();
            }
        })
        .map(new Func1<VirtualHubRouteTableV2Inner, VirtualHubRouteTableV2>() {
            @Override
            public VirtualHubRouteTableV2 call(VirtualHubRouteTableV2Inner inner) {
                return wrapModel(inner);
            }
        });
    }

    @Override
    public Observable<VirtualHubRouteTableV2> getAsync(String resourceGroupName, String virtualHubName, String routeTableName) {
        VirtualHubRouteTableV2sInner client = this.inner();
        return client.getAsync(resourceGroupName, virtualHubName, routeTableName)
        .flatMap(new Func1<VirtualHubRouteTableV2Inner, Observable<VirtualHubRouteTableV2>>() {
            @Override
            public Observable<VirtualHubRouteTableV2> call(VirtualHubRouteTableV2Inner inner) {
                if (inner == null) {
                    return Observable.empty();
                } else {
                    return Observable.just((VirtualHubRouteTableV2)wrapModel(inner));
                }
            }
       });
    }

    @Override
    public Completable deleteAsync(String resourceGroupName, String virtualHubName, String routeTableName) {
        VirtualHubRouteTableV2sInner client = this.inner();
        return client.deleteAsync(resourceGroupName, virtualHubName, routeTableName).toCompletable();
    }

}