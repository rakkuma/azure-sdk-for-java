/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.management.keyvault.implementation;

import com.microsoft.azure.management.graphrbac.Group;
import com.microsoft.azure.management.graphrbac.ServicePrincipal;
import com.microsoft.azure.management.graphrbac.User;
import com.microsoft.azure.management.keyvault.AccessPolicy;
import com.microsoft.azure.management.keyvault.AccessPolicyEntry;
import com.microsoft.azure.management.keyvault.KeyPermissions;
import com.microsoft.azure.management.keyvault.Permissions;
import com.microsoft.azure.management.keyvault.SecretPermissions;
import com.microsoft.azure.management.keyvault.Vault;
import com.microsoft.azure.management.resources.fluentcore.arm.models.implementation.ChildResourceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Implementation for StorageAccount and its parent interfaces.
 */
class AccessPolicyImpl
        extends ChildResourceImpl<
                AccessPolicyEntry,
                VaultImpl>
        implements
            AccessPolicy,
            AccessPolicy.Definition<Vault.DefinitionStages.WithCreate>,
            AccessPolicy.UpdateDefinition<Vault.Update>,
            AccessPolicy.Update {

    AccessPolicyImpl(AccessPolicyEntry innerObject, VaultImpl parent) {
        super(innerObject, parent);
    }

    @Override
    public UUID tenantId() {
        return inner().tenantId();
    }

    @Override
    public UUID objectId() {
        return inner().objectId();
    }

    @Override
    public UUID applicationId() {
        return inner().applicationId();
    }

    @Override
    public Permissions permissions() {
        return inner().permissions();
    }

    @Override
    public String name() {
        return inner().objectId().toString();
    }

    private void initializeKeyPermissions() {
        if (inner().permissions() == null) {
            inner().withPermissions(new Permissions());
        }
        if (inner().permissions().keys() == null) {
            inner().permissions().withKeys(new ArrayList<KeyPermissions>());
        }
    }

    private void initializeSecretPermissions() {
        if (inner().permissions() == null) {
            inner().withPermissions(new Permissions());
        }
        if (inner().permissions().secrets() == null) {
            inner().permissions().withSecrets(new ArrayList<SecretPermissions>());
        }
    }

    @Override
    public AccessPolicyImpl allowKeyPermissions(KeyPermissions... permissions) {
        initializeKeyPermissions();
        inner().permissions().keys().addAll(Arrays.asList(permissions));
        return this;
    }

    @Override
    public AccessPolicyImpl allowKeyPermissions(List<KeyPermissions> permissions) {
        initializeKeyPermissions();
        inner().permissions().keys().addAll(permissions);
        return this;
    }

    @Override
    public AccessPolicyImpl allowSecretPermissions(SecretPermissions... permissions) {
        initializeSecretPermissions();
        inner().permissions().secrets().addAll(Arrays.asList(permissions));
        return this;
    }

    @Override
    public AccessPolicyImpl allowSecretPermissions(List<SecretPermissions> permissions) {
        initializeSecretPermissions();
        inner().permissions().secrets().addAll(permissions);
        return this;
    }

    @Override
    public VaultImpl attach() {
        parent().withAccessPolicy(this);
        return parent();
    }

    @Override
    public AccessPolicyImpl forObjectId(UUID objectId) {
        inner().withObjectId(objectId);
        return this;
    }

    @Override
    public AccessPolicyImpl forUser(User user) {
        inner().withObjectId(UUID.fromString(user.objectId()));
        return this;
    }

    @Override
    public AccessPolicyImpl forGroup(Group group) {
        inner().withObjectId(UUID.fromString(group.objectId()));
        return this;
    }

    @Override
    public AccessPolicyImpl forServicePrincipal(ServicePrincipal servicePrincipal) {
        inner().withObjectId(UUID.fromString(servicePrincipal.objectId()));
        return this;
    }

    @Override
    public AccessPolicyImpl allowKeyAllPermissions() {
        initializeKeyPermissions();
        // TODO: Add all
        return this;
    }

    @Override
    public AccessPolicyImpl disallowKeyAllPermissions() {
        initializeKeyPermissions();
        inner().permissions().keys().clear();
        return this;
    }

    @Override
    public AccessPolicyImpl disallowKeyPermissions(KeyPermissions... permissions) {
        initializeSecretPermissions();
        inner().permissions().keys().removeAll(Arrays.asList(permissions));
        return this;
    }

    @Override
    public AccessPolicyImpl disallowKeyPermissions(List<KeyPermissions> permissions) {
        initializeSecretPermissions();
        inner().permissions().keys().removeAll(permissions);
        return this;
    }

    @Override
    public AccessPolicyImpl allowSecretAllPermissions() {
        // TODO: add all
        return null;
    }

    @Override
    public AccessPolicyImpl disallowSecretAllPermissions() {
        initializeSecretPermissions();
        inner().permissions().secrets().clear();
        return this;
    }

    @Override
    public AccessPolicyImpl disallowSecretPermissions(SecretPermissions... permissions) {
        initializeSecretPermissions();
        inner().permissions().secrets().removeAll(Arrays.asList(permissions));
        return this;
    }

    @Override
    public AccessPolicyImpl disallowSecretPermissions(List<SecretPermissions> permissions) {
        initializeSecretPermissions();
        inner().permissions().secrets().removeAll(permissions);
        return this;
    }

    @Override
    public String key() {
        return objectId().toString();
    }
}
