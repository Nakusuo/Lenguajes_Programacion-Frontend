package com.minerva.domain.entities.customer;

import com.minerva.domain.entities.shared.PhoneNumber;
import com.minerva.domain.entities.shared.Result;

import java.time.LocalDateTime;
import java.util.Objects;

public class Customer {
    private final CustomerId customerNameId;
    private PhoneNumber phoneNumber;
    private final LocalDateTime registrationDate;

    private Customer(CustomerId customerNameId, PhoneNumber phoneNumber) {
        this.customerNameId = customerNameId;
        this.phoneNumber = phoneNumber;
        // VALORES POR DEFECTO
        this.registrationDate = LocalDateTime.now();
    }

    public Result<Customer> create(String name, String phoneNumber) {
        Result<CustomerId> customerIdResult = CustomerId.of(name);
        if (customerIdResult.isFail()) return Result.fail(customerIdResult.getMessage());

        Result<PhoneNumber> phoneNumberResult = PhoneNumber.of(phoneNumber);
        if (phoneNumberResult.isFail()) return Result.fail(phoneNumberResult.getMessage());


        return Result.success(new Customer(customerIdResult.getData(), phoneNumberResult.getData()));
    }

    public CustomerId getCustomerNameId() {
        return customerNameId;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
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
