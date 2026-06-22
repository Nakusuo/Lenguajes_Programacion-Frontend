package com.minerva.domain.constants;

import java.util.Set;

public enum Role {

    ADMIN(Set.of(
            Permission.Test
    )),
    VENDEDOR(Set.of(
            Permission.Test
    )),
    ALMACENISTA(Set.of(
            Permission.Test
    ));

    private final Set<Permission> permissions;

    private Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public boolean hasPermission (Permission permission) {
        return permissions.contains(permission);
    }
}
