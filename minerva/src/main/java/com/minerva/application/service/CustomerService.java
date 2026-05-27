package com.minerva.application.service;

import java.util.List;
import java.util.Optional;

import com.minerva.domain.repositories.CustomerRepository;
import com.minerva.domain.entities.customer.Customer;
import com.minerva.domain.entities.customer.CustomerId;
import com.minerva.domain.entities.shared.PhoneNumber;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.exceptions.DomainException;

public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Result<Void> registerCustomer(String customerName, String phoneNumber) {
        try {
            Customer customerCreated = new Customer(customerName, phoneNumber);
            if (customerRepository.existsById(customerCreated.getCustomerNameId()))
                return Result.fail("Ya existe un cliente con el mismo nombre.");

            if (customerCreated.getPhoneNumber().isPresent() && customerRepository.existsByPhoneNumber(customerCreated.getPhoneNumber().get()))
                return Result.fail("Ya existe un cliente con el mismo número de teléfono.");

            customerRepository.save(customerCreated);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }

        return Result.success(null);
    }

    public Optional<Customer> findCustomerById(String customerId) {
        try {
            return customerRepository.findById(new CustomerId(customerId));
        } catch (DomainException e) {
            return Optional.empty();
        }
    }

    public Optional<Customer> findCustomerByPhoneNumber(String phoneNumber) {
        try {
            return customerRepository.findByPhoneNumber(new PhoneNumber(phoneNumber));
        } catch (DomainException e) {
            return Optional.empty();
        }
    }

    public Result<Void> updatePhoneNumber(String customerId, String newPhoneNumber) {
        try {
            Optional<Customer> customerOpt = customerRepository.findById(new CustomerId(customerId));

            if (customerOpt.isEmpty()) return Result.fail("Cliente no encontrado.");                

            Customer customer = customerOpt.get();
            customer.updatePhoneNumber(newPhoneNumber);
            customerRepository.save(customer);   
            return Result.success(null);         
        } catch (DomainException e) {
            return Result.fail("Cliente no encontrado.");
        }
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

}
