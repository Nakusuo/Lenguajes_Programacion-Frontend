package com.minerva.infrastructure.rest.config;

import com.minerva.application.service.*;
import com.minerva.domain.interfaces.PasswordHasher;
import com.minerva.domain.repositories.*;
import com.minerva.infrastructure.adapter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CustomerService customerService(CustomerRepository customerRepository) {
        return new CustomerService(customerRepository);
    }   

    @Bean
    public ProductService productService(ProductRepository productRepository, SupplierRepository supplierRepository) {
        return new ProductService(productRepository, supplierRepository);
    }

    @Bean
    public SaleService saleService(SaleRepository saleRepository, CustomerRepository customerRepository) {
        return new SaleService(saleRepository, customerRepository);
    }

    @Bean
    public SupplierService supplierService(SupplierRepository supplierRepository) {
        return new SupplierService(supplierRepository);
    }

    @Bean
    public UserService userService(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new UserService(userRepository, passwordHasher);
    }

    @Bean
    public PasswordHasher passwordHasher(){
        return new PasswordHasherAdapter();
    }
}
