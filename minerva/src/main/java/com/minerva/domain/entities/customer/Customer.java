package com.minerva.domain.entities.customer;

import com.minerva.domain.entities.shared.PhoneNumber;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.entities.shared.Result;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Customer {
    private final CustomerId customerNameId;
    // Puede ser null
    private PhoneNumber phoneNumber;
    // ------------
    private final LocalDateTime registrationDate;

    public Customer(String name, String phoneNumber) throws DomainException {

        this.customerNameId = new CustomerId(name);
        this.phoneNumber = (phoneNumber != null)
                ? new PhoneNumber(phoneNumber)
                : null;

        // VALORES POR DEFECTO
        this.registrationDate = LocalDateTime.now();
    }

    public CustomerId getCustomerNameId() {
        return customerNameId;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerNameId, customer.customerNameId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(customerNameId);
    }
}
