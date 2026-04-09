CREATE DATABASE apolo;
USE apolo;

CREATE TABLE supplier (
    supplierNameId VARCHAR(100) PRIMARY KEY,
    ruc CHAR(11) unique,
    phoneNumber CHAR(9) unique,
    registrationDate TIMESTAMP NOT NULL 

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE customer (
    customerNameId VARCHAR(100) PRIMARY KEY,
    phoneNumber CHAR(9) UNIQUE,
    registrationDate TIMESTAMP NOT NULL

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9 campos
CREATE TABLE product (
    productNameId VARCHAR(100) PRIMARY KEY,--
    gainStrategy ENUM('PORCENTAJE', 'INCREMENTAL') NOT NULL,--
    gainAmount DECIMAL(10,2) NOT NULL,--
    stock DECIMAL(10,3) NOT NULL, --
    reorderLevel DECIMAL(10,3),
    barCode CHAR(13) UNIQUE,
    SaleType ENUM('UNIDAD', 'GRANEL') NOT NULL,--
    category ENUM(
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
    ) NOT NULL,    --
    registrationDate TIMESTAMP NOT NULL--

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4 campos
CREATE TABLE unitToBulk (    
    unitProductNameId VARCHAR(100) NOT NULL,
    quantity DECIMAL(10,3) NOT NULL,

    bulkProductNameId VARCHAR(100) NOT NULL UNIQUE,  
    
    registrationDate TIMESTAMP NOT NULL,

    PRIMARY KEY (bulkProductNameId, unitProductNameId),

    CONSTRAINT fk_bulk_product FOREIGN KEY (bulkProductNameId)
        REFERENCES product(productNameId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_unit_product FOREIGN KEY (unitProductNameId)
        REFERENCES product(productNameId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7 campos
CREATE TABLE stockEntry (
    stockEntryId CHAR(36) PRIMARY KEY,
    productNameId VARCHAR(100) NOT NULL,
    supplierNameId VARCHAR(100) NOT NULL,
    priceUnit DECIMAL(10,2) NOT NULL,
    quantity DECIMAL(10,3) NOT NULL,
    ExpirationDate TIMESTAMP,
    registrationDate TIMESTAMP NOT NULL,
 
    CONSTRAINT fk_stockEntry_product FOREIGN KEY (productNameId)
        REFERENCES product(productNameId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_stockEntry_supplier FOREIGN KEY (supplierNameId)
        REFERENCES supplier(supplierNameId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
        
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3 campos
CREATE TABLE sale (
    saleId CHAR(36) PRIMARY KEY,
    customerNameId VARCHAR(100) NOT NULL,
    registrationDate TIMESTAMP NOT NULL,

    CONSTRAINT fk_sale_customerNameId FOREIGN KEY (customerNameId)
        REFERENCES customer(customerNameId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE saleDetail (
    saleDetailId CHAR(36) PRIMARY KEY,
    saleId CHAR(36) NOT NULL,
    productNameId VARCHAR(100) NOT NULL,
    quantity DECIMAL(10,3) NOT NULL,
    priceUnit DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_sd_sale FOREIGN KEY (saleId)
        REFERENCES sale(saleId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_sd_product FOREIGN KEY (productNameId)
        REFERENCES product(productNameId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE inventoryLoss (
    inventoryLossId CHAR(36) PRIMARY KEY,
    productNameId VARCHAR(100) NOT NULL,
    quantity DECIMAL(10,3) NOT NULL,
    reason ENUM('DAÑADO', 'VENCIMIENTO', 'PERDIDO', 'COMSUMO', 'DRAKO', 'ROBO', 'OTROS') NOT NULL,
    observation VARCHAR(255),
    registrationDate TIMESTAMP NOT NULL,

    CONSTRAINT fk_inventoryLoss_product FOREIGN KEY (productNameId)
        REFERENCES product(productNameId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE pay (
  payId CHAR(36) PRIMARY KEY,
  saleId CHAR(36) NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  paymentMethod ENUM('EFECTIVO', 'DIGITAL') NOT NULL,
  registrationDate TIMESTAMP NOT NULL,

  CONSTRAINT fk_pay_sale FOREIGN KEY (saleId)
    REFERENCES sale(saleId)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE productReturn (
    productReturnId CHAR(36) PRIMARY KEY,
    saleDetailId CHAR(36) NOT NULL,
    quantity DECIMAL(10,3) NOT NULL,
    reason ENUM('DAÑADO', 'VENCIDO', 'EQUIVOCACION','OTROS') NOT NULL,
    registrationDate TIMESTAMP NOT NULL,

    CONSTRAINT fk_return_sale FOREIGN KEY (saleDetailId)
        REFERENCES saleDetail(saleDetailId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Datos iniciales
INSERT INTO supplier (supplierNameId) VALUES ('anonimo');
INSERT INTO customer (customerNameId) VALUES ('anonimo');

-- TRIGGERS
-- SHOW TRIGGERS

