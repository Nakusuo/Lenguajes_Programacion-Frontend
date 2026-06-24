package com.minerva.domain.constants;

public enum Permission {

    // Customer - Write
    CUSTOMER_REGISTER,
    CUSTOMER_UPDATE_PHONE_NUMBER,

    // Customer - Read
    CUSTOMER_FIND_BY_ID,
    CUSTOMER_FIND_BY_PHONE_NUMBER,
    CUSTOMER_GET_ALL,
//---------------------------------------------------------
    // Product - Write
    PRODUCT_REGISTER,
    PRODUCT_REGISTER_STOCK_ENTRY,
    PRODUCT_ASSOCIATE_UNIT_TO_BULK,

    // Product - Read
    PRODUCT_FIND_BY_ID,
    PRODUCT_FIND_BY_BAR_CODE,
    PRODUCT_FIND_ALL,

//---------------------------------------------------------

    // Sale - Write
    SALE_REGISTER,
    SALE_ADD_PAYMENT,

    // Sale - Read
    SALE_FIND_BY_ID,
    SALE_FIND_BY_CUSTOMER_ID,
    SALE_FIND_ALL,
//---------------------------------------------------------
    // Supplier - Write
    SUPPLIER_REGISTER,
    SUPPLIER_UPDATE_PHONE_NUMBER,
    SUPPLIER_UPDATE_RUC,

    // Supplier - Read
    SUPPLIER_FIND_ALL,
    SUPPLIER_FIND_BY_ID,
    SUPPLIER_FIND_BY_RUC,
    SUPPLIER_FIND_BY_PHONE_NUMBER,
//---------------------------------------------------------

    // User - Write
    USER_REGISTER,

    // User - Auth
    USER_AUTHENTICATE,

    // User - Read
    USER_FIND_BY_USERNAME,
    USER_FIND_BY_ID,
    USER_FIND_ALL
}
