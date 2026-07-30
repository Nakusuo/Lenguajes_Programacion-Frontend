-- ==========================
-- CREAR BASE DE DATOS
-- ==========================

CREATE DATABASE apolo;

-- Conectarse a la base de datos
-- \c apolo

-- ==========================
-- ENUMS
-- ==========================

CREATE TYPE gain_strategy AS ENUM (
    'PORCENTAJE',
    'INCREMENTAL'
);

CREATE TYPE sale_type AS ENUM (
    'UNIDAD',
    'GRANEL'
);

CREATE TYPE product_category AS ENUM (
    'BEBIDAS',
    'ABARROTES_SECOS',
    'CAFE_INFUSIONES',
    'LACTEOS',
    'CARNES',
    'SNACKS_GOLOSINAS',
    'CUIDADO_PERSONAL',
    'LIMPIEZA_HOGAR',
    'BEBÉS',
    'MASCOTAS',
    'OTROS'
);

CREATE TYPE inventory_loss_reason AS ENUM (
    'DAÑADO',
    'VENCIMIENTO',
    'PERDIDO',
    'ROBO',
    'OTROS'
);

CREATE TYPE role AS ENUM (
    'ADMIN',
    'VENDEDOR',
    'ALMACENISTA'
);

CREATE TYPE permission AS ENUM (
    -- Customer - Write
    'CUSTOMER_REGISTER',
    'CUSTOMER_UPDATE_PHONE_NUMBER',

    -- Customer - Read
    'CUSTOMER_FIND_BY_ID',
    'CUSTOMER_FIND_BY_PHONE_NUMBER',
    'CUSTOMER_GET_ALL',

    -- Product - Write
    'PRODUCT_REGISTER',
    'PRODUCT_REGISTER_STOCK_ENTRY',
    'PRODUCT_ASSOCIATE_UNIT_TO_BULK',

    -- Product - Read
    'PRODUCT_FIND_BY_ID',
    'PRODUCT_FIND_BY_BAR_CODE',
    'PRODUCT_FIND_ALL',

    -- Sale - Write
    'SALE_REGISTER',
    'SALE_ADD_PAYMENT',

    -- Sale - Read
    'SALE_FIND_BY_ID',
    'SALE_FIND_BY_CUSTOMER_ID',
    'SALE_FIND_ALL',

    -- Supplier - Write
    'SUPPLIER_REGISTER',
    'SUPPLIER_UPDATE_PHONE_NUMBER',
    'SUPPLIER_UPDATE_RUC',

    -- Supplier - Read
    'SUPPLIER_FIND_ALL',
    'SUPPLIER_FIND_BY_ID',
    'SUPPLIER_FIND_BY_RUC',
    'SUPPLIER_FIND_BY_PHONE_NUMBER',

    -- User - Write
    'USER_REGISTER',

    -- User - Auth
    'USER_AUTHENTICATE',

    -- User - Read
    'USER_FIND_BY_USERNAME',
    'USER_FIND_BY_ID',
    'USER_FIND_ALL'
);

CREATE TYPE payment_method AS ENUM (
    'EFECTIVO',
    'DIGITAL'
);

CREATE TYPE return_reason AS ENUM (
    'DAÑADO',
    'VENCIDO',
    'EQUIVOCACION',
    'OTROS'
);

-- ==========================
-- TABLAS
-- ==========================

CREATE TABLE app_user (
    user_name VARCHAR(30) PRIMARY KEY,
    dni CHAR(8) UNIQUE NOT NULL,
    names VARCHAR(50) NOT NULL,
    last_names VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_name role NOT NULL,
    is_active BOOLEAN NOT NULL,
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE user_action (
    user_action_id UUID PRIMARY KEY,
    user_name VARCHAR(30) NOT NULL,
    permission permission NOT NULL,
    entity_id TEXT NOT NULL,
    entity_data JSONB NOT NULL,
    registration_date TIMESTAMP NOT NULL,

    CONSTRAINT fk_user_action_user
        FOREIGN KEY (user_name)
        REFERENCES app_user(user_name)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE supplier (
    supplier_name_id VARCHAR(100) PRIMARY KEY,
    ruc CHAR(11) UNIQUE,
    phone_number CHAR(9) UNIQUE,
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE customer (
    customer_name_id VARCHAR(50) PRIMARY KEY,
    phone_number CHAR(9) UNIQUE,
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE product (
    product_id UUID PRIMARY KEY,
    sku VARCHAR(100) NOT NULL UNIQUE,
    product_name VARCHAR(100) NOT NULL UNIQUE,
    gain_strategy gain_strategy NOT NULL,
    gain_amount NUMERIC(10,2) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    stock NUMERIC(10,3) NOT NULL,
    reorder_level NUMERIC(10,3),
    bar_code CHAR(13) UNIQUE,
    sale_type sale_type NOT NULL,
    category product_category NOT NULL,
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE unit_to_bulk (
    unit_product_id UUID NOT NULL,
    quantity NUMERIC(10,3) NOT NULL,
    bulk_product_id UUID NOT NULL UNIQUE,
    registration_date TIMESTAMP NOT NULL,

    PRIMARY KEY (bulk_product_id, unit_product_id),

    CONSTRAINT fk_bulk_product
        FOREIGN KEY (bulk_product_id)
        REFERENCES product(product_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_unit_product
        FOREIGN KEY (unit_product_id)
        REFERENCES product(product_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE stock_entry (
    stock_entry_id UUID PRIMARY KEY,
    id_product UUID NOT NULL,
    id_supplier_name VARCHAR(100) NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    quantity NUMERIC(10,3) NOT NULL,
    expiration_date TIMESTAMP,
    registration_date TIMESTAMP NOT NULL,

    CONSTRAINT fk_stock_entry_product
        FOREIGN KEY (id_product)
        REFERENCES product(product_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_stock_entry_supplier
        FOREIGN KEY (id_supplier_name)
        REFERENCES supplier(supplier_name_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE sale (
    sale_id UUID PRIMARY KEY,
    id_customer_name VARCHAR(50) NOT NULL,
    registration_date TIMESTAMP NOT NULL,

    CONSTRAINT fk_sale_customer
        FOREIGN KEY (id_customer_name)
        REFERENCES customer(customer_name_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE sale_detail (
    sale_detail_id UUID PRIMARY KEY,
    id_sale UUID NOT NULL,
    id_product UUID NOT NULL,
    quantity NUMERIC(10,3) NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,

    CONSTRAINT fk_sale_detail_sale
        FOREIGN KEY (id_sale)
        REFERENCES sale(sale_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_sale_detail_product
        FOREIGN KEY (id_product)
        REFERENCES product(product_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE inventory_loss (
    inventory_loss_id UUID PRIMARY KEY,
    id_product UUID NOT NULL,
    quantity NUMERIC(10,3) NOT NULL,
    reason inventory_loss_reason NOT NULL,
    observation VARCHAR(255),
    registration_date TIMESTAMP NOT NULL,

    CONSTRAINT fk_inventory_loss_product
        FOREIGN KEY (id_product)
        REFERENCES product(product_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE pay (
    pay_id UUID PRIMARY KEY,
    id_sale UUID NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    payment_method payment_method NOT NULL,
    registration_date TIMESTAMP NOT NULL,

    CONSTRAINT fk_pay_sale
        FOREIGN KEY (id_sale)
        REFERENCES sale(sale_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE product_return (
    product_return_id UUID PRIMARY KEY,
    id_sale_detail UUID NOT NULL,
    quantity NUMERIC(10,3) NOT NULL,
    reason return_reason NOT NULL,
    registration_date TIMESTAMP NOT NULL,

    CONSTRAINT fk_product_return_sale_detail
        FOREIGN KEY (id_sale_detail)
        REFERENCES sale_detail(sale_detail_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- ==========================
-- DATOS INICIALES
-- ==========================

INSERT INTO supplier (supplier_name_id, registration_date)
VALUES ('anonimo', NOW());

INSERT INTO customer (customer_name_id, registration_date)
VALUES ('anonimo', NOW());

-- ==========================
-- AUDITORIA 
-- AJENO A LAS REGLAS DE NEGOCIO, EN CONSECUENCIA SE TOMARON MAS LIBERTADES CON RESPECTO AL TRATAMIENTO DE LOS DATOS
-- ==========================

CREATE TABLE audit_log (
    audit_id BIGSERIAL PRIMARY KEY,
    table_name TEXT NOT NULL,
    operation CHAR(1) NOT NULL CHECK (operation IN ('I', 'U', 'D')),
    record_id TEXT,
    old_data JSONB,
    new_data JSONB,
    user_name TEXT,
    audit_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE FUNCTION audit_trigger()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    entity_id TEXT;
BEGIN
    IF TG_OP = 'INSERT' THEN
        entity_id := to_jsonb(NEW)->>TG_ARGV[0];

        INSERT INTO audit_log(
            table_name,
            operation,
            record_id,
            user_name,
            new_data
        )
        VALUES (
            TG_TABLE_NAME,
            'I',
            entity_id,
            current_user,
            to_jsonb(NEW)
        );

        RETURN NEW;

    ELSIF TG_OP = 'UPDATE' THEN
        entity_id := to_jsonb(NEW)->>TG_ARGV[0];

        INSERT INTO audit_log(
            table_name,
            operation,
            record_id,
            old_data,
            new_data,
            user_name
        )
        VALUES (
            TG_TABLE_NAME,
            'U',
            entity_id,
            to_jsonb(OLD),
            to_jsonb(NEW),
            current_user
        );

        RETURN NEW;

    ELSE
        entity_id := to_jsonb(OLD)->>TG_ARGV[0];

        INSERT INTO audit_log(
            table_name,
            operation,
            record_id,
            old_data,
            user_name
        )
        VALUES (
            TG_TABLE_NAME,
            'D',
            entity_id,
            to_jsonb(OLD),
            current_user
        );

        RETURN OLD;
    END IF;
END;
$$;

CREATE TRIGGER trg_audit_app_user
AFTER INSERT OR UPDATE OR DELETE ON app_user
FOR EACH ROW
EXECUTE FUNCTION audit_trigger('user_name');

CREATE TRIGGER trg_audit_supplier
AFTER INSERT OR UPDATE OR DELETE ON supplier
FOR EACH ROW
EXECUTE FUNCTION audit_trigger('supplier_name_id');

CREATE TRIGGER trg_audit_customer
AFTER INSERT OR UPDATE OR DELETE ON customer
FOR EACH ROW
EXECUTE FUNCTION audit_trigger('customer_name_id');

CREATE TRIGGER trg_audit_product
AFTER INSERT OR UPDATE OR DELETE ON product
FOR EACH ROW
EXECUTE FUNCTION audit_trigger('product_id');

CREATE TRIGGER trg_audit_unit_to_bulk
AFTER INSERT OR UPDATE OR DELETE ON unit_to_bulk
FOR EACH ROW
EXECUTE FUNCTION audit_trigger('bulk_product_id');

CREATE TRIGGER trg_audit_stock_entry
AFTER INSERT OR UPDATE OR DELETE ON stock_entry
FOR EACH ROW
EXECUTE FUNCTION audit_trigger('stock_entry_id');

CREATE TRIGGER trg_audit_sale
AFTER INSERT OR UPDATE OR DELETE ON sale
FOR EACH ROW
EXECUTE FUNCTION audit_trigger('sale_id');

CREATE TRIGGER trg_audit_sale_detail
AFTER INSERT OR UPDATE OR DELETE ON sale_detail
FOR EACH ROW
EXECUTE FUNCTION audit_trigger('sale_detail_id');

CREATE TRIGGER trg_audit_inventory_loss
AFTER INSERT OR UPDATE OR DELETE ON inventory_loss
FOR EACH ROW
EXECUTE FUNCTION audit_trigger('inventory_loss_id');

CREATE TRIGGER trg_audit_pay
AFTER INSERT OR UPDATE OR DELETE ON pay
FOR EACH ROW
EXECUTE FUNCTION audit_trigger('pay_id');

CREATE TRIGGER trg_audit_product_return
AFTER INSERT OR UPDATE OR DELETE ON product_return
FOR EACH ROW
EXECUTE FUNCTION audit_trigger('product_return_id');

CREATE TRIGGER trg_audit_user_action
AFTER INSERT OR UPDATE OR DELETE ON user_action
FOR EACH ROW
EXECUTE FUNCTION audit_trigger('user_action_id');