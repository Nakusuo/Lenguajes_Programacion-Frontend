package com.minerva.application.service;

import java.util.List;
import java.util.Optional;

import com.minerva.domain.constants.Permission;
import com.minerva.domain.constants.Role;
import com.minerva.domain.repositories.CustomerRepository;
import com.minerva.domain.entities.customer.Customer;
import com.minerva.domain.entities.result.Result;
import com.minerva.domain.repositories.UserRepository;
import com.minerva.domain.valueObject.id.AllId;
import com.minerva.domain.valueObject.id.CustomerName;
import com.minerva.domain.valueObject.PhoneNumber;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnauthorizedActionException;
import com.minerva.domain.valueObject.id.UserName;

public class CustomerService extends Service {
    private final CustomerRepository customerRepository;

    public CustomerService(Role userRole, UserName userName, UserRepository userRepository, CustomerRepository customerRepository) {
        super(userRole, userName, userRepository);
        this.customerRepository = customerRepository;
    }

    // --------------------- WRITE ---------------------
    public Result<Void> registerCustomer(String customerName, String phoneNumber) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.CUSTOMER_REGISTER)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para registrar clientes.");
        
        Customer customerCreated;
        try {
            customerCreated = new Customer(customerName, phoneNumber);
            if (customerRepository.existsById(customerCreated.getCustomerName()))
                return Result.fail("Ya existe un cliente con el mismo nombre.");

            if (customerCreated.getPhoneNumber().isPresent() && customerRepository.existsByPhoneNumber(customerCreated.getPhoneNumber().get()))
                return Result.fail("Ya existe un cliente con el mismo número de teléfono.");

            customerRepository.save(customerCreated);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }

        registerUserAction(Permission.CUSTOMER_REGISTER, customerCreated.getId());
        return Result.success(null);
    }

    public Result<Void> updatePhoneNumber(String customerId, String newPhoneNumber) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.CUSTOMER_UPDATE_PHONE_NUMBER)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para actualizar el número de teléfono del cliente.");

        try {
            Optional<Customer> customerOpt = customerRepository.findById(new CustomerName(customerId));

            if (customerOpt.isEmpty()) return Result.fail("Cliente no encontrado.");                

            Customer customer = customerOpt.get();
            
            Result<Void> updatePhoneNumberResult = customer.updatePhoneNumber(newPhoneNumber);
            if (updatePhoneNumberResult.isFail()) return updatePhoneNumberResult;

            if (customerRepository.existsByPhoneNumber(new PhoneNumber(newPhoneNumber)))
                return Result.fail("Ya existe un cliente con el mismo número de teléfono.");

            customerRepository.save(customer);  
            registerUserAction(Permission.CUSTOMER_UPDATE_PHONE_NUMBER, customer.getId()); 
            return Result.success(null);         
        } catch (DomainException e) {
            return Result.fail("Cliente no encontrado.");
        }
    }

    // --------------------- READ ---------------------
    public Optional<Customer> findCustomerById(String customerId) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.CUSTOMER_FIND_BY_ID)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para buscar clientes por ID.");
        
        try {
            CustomerName customerName = new CustomerName(customerId);
            registerUserAction(Permission.CUSTOMER_FIND_BY_ID, customerName);
            return customerRepository.findById(customerName);
        } catch (DomainException e) {
            return Optional.empty();
        }
    }

    public Optional<Customer> findCustomerByPhoneNumber(String phoneNumber) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.CUSTOMER_FIND_BY_PHONE_NUMBER)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para buscar clientes por número de teléfono.");

        try {            
            return customerRepository
                        .findByPhoneNumber(new PhoneNumber(phoneNumber))
                        .map(customer -> {
                            registerUserAction(
                                Permission.CUSTOMER_FIND_BY_PHONE_NUMBER,
                                customer.getId()
                            );
                            return customer;
                        });
        } catch (DomainException e) {
            return Optional.empty();
        }
    }

    public List<Customer> getAllCustomers() throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.CUSTOMER_GET_ALL)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para obtener todos los clientes.");
        
        List<Customer> customers = customerRepository.findAll();
        registerUserAction(Permission.CUSTOMER_GET_ALL, new AllId());
        return customers;
    }

}
