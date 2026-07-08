package com.minerva.domain.constants;

import java.util.Set;

public enum Role {

    ADMIN(Set.of(
            Permission.values()
    )),
    VENDEDOR(Set.of(
            Permission.CUSTOMER_REGISTER,
            Permission.CUSTOMER_UPDATE_PHONE_NUMBER,
            Permission.CUSTOMER_FIND_BY_ID,
            Permission.CUSTOMER_FIND_BY_PHONE_NUMBER,
            Permission.CUSTOMER_GET_ALL,
            Permission.PRODUCT_FIND_BY_ID,
            Permission.PRODUCT_FIND_BY_BAR_CODE,
            Permission.PRODUCT_FIND_ALL,
            Permission.SALE_REGISTER,
            Permission.SALE_ADD_PAYMENT,
            Permission.SALE_FIND_BY_ID,
            Permission.SALE_FIND_BY_CUSTOMER_ID,
            Permission.SALE_FIND_ALL
    )),
    ALMACENISTA(Set.of(
            Permission.PRODUCT_REGISTER,
            Permission.PRODUCT_REGISTER_STOCK_ENTRY,
            Permission.PRODUCT_ASSOCIATE_UNIT_TO_BULK,
            Permission.PRODUCT_FIND_BY_ID,
            Permission.PRODUCT_FIND_BY_BAR_CODE,
            Permission.PRODUCT_FIND_ALL,
            Permission.SUPPLIER_REGISTER,
            Permission.SUPPLIER_UPDATE_PHONE_NUMBER,
            Permission.SUPPLIER_UPDATE_RUC,
            Permission.SUPPLIER_FIND_ALL,
            Permission.SUPPLIER_FIND_BY_ID,
            Permission.SUPPLIER_FIND_BY_RUC,
            Permission.SUPPLIER_FIND_BY_PHONE_NUMBER
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

    public boolean lacksPermission(Permission permission) {
        return !hasPermission(permission);
    }
}
