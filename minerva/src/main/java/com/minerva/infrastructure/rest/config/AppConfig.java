package com.minerva.infrastructure.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.minerva.application.service.CustomerService;
import com.minerva.application.service.ProductService;
import com.minerva.application.service.SaleService;
import com.minerva.application.service.SupplierService;
import com.minerva.domain.repositories.CustomerRepository;
import com.minerva.domain.repositories.ProductRepository;
import com.minerva.domain.repositories.SaleRepository;
import com.minerva.domain.repositories.SupplierRepository;
import com.minerva.infrastructure.adapter.CustomerRepositoryAdapter;
import com.minerva.infrastructure.adapter.ProductRepositoryAdapter;
import com.minerva.infrastructure.adapter.SaleRepositoryAdapter;
import com.minerva.infrastructure.adapter.SupplierRepositoryAdapter;
import com.minerva.infrastructure.persistence.repository.JpaCustomerRepository;
import com.minerva.infrastructure.persistence.repository.JpaPayRepository;
import com.minerva.infrastructure.persistence.repository.JpaProductRepository;
import com.minerva.infrastructure.persistence.repository.JpaSaleDetailRepository;
import com.minerva.infrastructure.persistence.repository.JpaSaleRepository;
import com.minerva.infrastructure.persistence.repository.JpaStockEntryRepository;
import com.minerva.infrastructure.persistence.repository.JpaSupplierRepository;
import com.minerva.infrastructure.persistence.repository.JpaUnitToBulkRepository;

@Configuration
public class AppConfig {
    @Bean
    public CustomerService customerService(CustomerRepository customerRepository) {
        return new CustomerService(customerRepository);
    }   

    @Bean
    public CustomerRepository customerRepository(JpaCustomerRepository customerRepository) {
        return new CustomerRepositoryAdapter(customerRepository);
    }

    @Bean
    public ProductService productService(ProductRepository productRepository, SupplierRepository supplierRepository) {
        return new ProductService(productRepository, supplierRepository);
    }

    @Bean
    public ProductRepository productRepository(JpaProductRepository jpaProductRepository, JpaStockEntryRepository jpaStockEntryRepository, JpaUnitToBulkRepository jpaUnitToBulkRepository) {
        return new ProductRepositoryAdapter(jpaProductRepository, jpaStockEntryRepository, jpaUnitToBulkRepository);
    }


    @Bean
    public SupplierRepository supplierRepository(JpaSupplierRepository jpaSupplierRepository) {
        return new SupplierRepositoryAdapter(jpaSupplierRepository);
    }

    @Bean
    public SaleService saleService(SaleRepository saleRepository, CustomerRepository customerRepository) {
        return new SaleService(saleRepository, customerRepository);
    }

    @Bean
    public SaleRepository saleRepository(JpaSaleRepository jpaSaleRepository, JpaPayRepository jpaPayRepository, JpaSaleDetailRepository jpaSaleDetailRepository) {
        return new SaleRepositoryAdapter(jpaSaleRepository, jpaPayRepository, jpaSaleDetailRepository);
    }

    @Bean
    public SupplierService supplierService(SupplierRepository supplierRepository) {
        return new SupplierService(supplierRepository);
    }
}
// Consider defining a bean of type 'com.minerva.application.service.CustomerService' 
// in your configuration.
