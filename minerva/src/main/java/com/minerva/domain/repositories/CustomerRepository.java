package com.minerva.domain.repositories;


import com.minerva.domain.entities.customer.Customer;
import com.minerva.domain.valueObject.id.CustomerId;
import com.minerva.domain.valueObject.PhoneNumber;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    void save(Customer customer);
    boolean existsById(CustomerId id);
    boolean existsByPhoneNumber(PhoneNumber phoneNumber);
    Optional<Customer> findById(CustomerId id);
    Optional<Customer> findByPhoneNumber(PhoneNumber phoneNumber);
    List<Customer> findAll();

}
