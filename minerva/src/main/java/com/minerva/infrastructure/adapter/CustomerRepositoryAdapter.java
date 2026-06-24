package com.minerva.infrastructure.adapter;

import com.minerva.domain.entities.customer.Customer;
import com.minerva.domain.entities.customer.CustomerId;
import com.minerva.domain.valueObject.PhoneNumber;
import com.minerva.domain.repositories.CustomerRepository;
import com.minerva.infrastructure.persistence.entity.CustomerEntity;
import com.minerva.infrastructure.persistence.repository.JpaCustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final JpaCustomerRepository jpaCustomerRepository;

    public CustomerRepositoryAdapter(JpaCustomerRepository jpaCustomerRepository) {
        this.jpaCustomerRepository = jpaCustomerRepository;
    }

    @Override
    public void save(Customer customer) {
        jpaCustomerRepository.save(toEntity(customer));
    }

    @Override
    public boolean existsById(CustomerId id) {
        return jpaCustomerRepository.existsById(id.value());
    }

    @Override
    public boolean existsByPhoneNumber(PhoneNumber phoneNumber) {
        return jpaCustomerRepository.existsByPhoneNumber(phoneNumber.value);
    }

    @Override
    public Optional<Customer> findById(CustomerId id) {
        return jpaCustomerRepository.findById(id.value())
                .map(this::toDomain);
    }

    @Override
    public Optional<Customer> findByPhoneNumber(PhoneNumber phoneNumber) {
        return jpaCustomerRepository.findByPhoneNumber(phoneNumber.value)
                .map(this::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return jpaCustomerRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Customer toDomain(CustomerEntity entity) {
        return new Customer(
                entity.getCustomerNameId(),
                entity.getRegistrationDate(),
                entity.getPhoneNumber()
        );
    }

    private CustomerEntity toEntity(Customer customer) {
        return new CustomerEntity(
                customer.getCustomerName().value,
                customer.getPhoneNumber().orElse(null).value,
                customer.getRegistrationDate()
        );
    }

}
