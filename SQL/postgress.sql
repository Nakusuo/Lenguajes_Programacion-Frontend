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
    role_name VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL,
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE user_action (
    user_action_id UUID PRIMARY KEY,
    user_name VARCHAR(30) NOT NULL,
    permission VARCHAR(50) NOT NULL,
    entity_id VARCHAR(100) NOT NULL,
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