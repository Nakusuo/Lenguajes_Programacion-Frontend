package com.minerva.domain.entities.customer;

import com.minerva.domain.interfaces.Entity;
import com.minerva.domain.valueObject.PhoneNumber;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.valueObject.id.CustomerName;

import java.time.LocalDateTime;
import java.util.Optional;

public class Customer extends Entity<CustomerId> {
    private final CustomerName customerName;
    // Puede ser null
    private PhoneNumber phoneNumber;
    // ------------
    private final LocalDateTime registrationDate;

    public Customer(String name, String phoneNumber) throws DomainException {
        CustomerName tempId = new CustomerName(name);
        super(tempId);
        this.customerName = tempId;
        this.phoneNumber = (phoneNumber != null)
                ? new PhoneNumber(phoneNumber)
                : null;

        // VALORES POR DEFECTO
        this.registrationDate = LocalDateTime.now();
    }

    public Customer(String customerName, LocalDateTime registrationDate, String phoneNumber) {
        CustomerName tempId;
        try {
            tempId = new CustomerName(customerName);
            this.customerName = tempId;
            this.registrationDate = registrationDate;
            this.phoneNumber = phoneNumber != null ? new PhoneNumber(phoneNumber) : null;
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al crear el cliente: " + e.getMessage(), e);
        }
        super(tempId);
    }

    public CustomerName getCustomerName() {
        return customerName;
    }

    public Optional<PhoneNumber> getPhoneNumber() {
        if (phoneNumber == null) return Optional.empty();
        return Optional.of(phoneNumber);
    }

    public Result<Void> updatePhoneNumber(String newPhoneNumber) {
        try {
            this.phoneNumber = (newPhoneNumber != null)
                ? new PhoneNumber(newPhoneNumber)
                : null;
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
        return Result.success(null);
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

}
