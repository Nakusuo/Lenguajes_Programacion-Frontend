package com.minerva.domain.repositories;


import com.minerva.domain.entities.customer.Customer;
import com.minerva.domain.valueObject.id.CustomerName;
import com.minerva.domain.valueObject.PhoneNumber;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    void save(Customer customer);
    boolean existsById(CustomerName id);
    boolean existsByPhoneNumber(PhoneNumber phoneNumber);
    Optional<Customer> findById(CustomerName id);
    Optional<Customer> findByPhoneNumber(PhoneNumber phoneNumber);
    List<Customer> findAll();

}
