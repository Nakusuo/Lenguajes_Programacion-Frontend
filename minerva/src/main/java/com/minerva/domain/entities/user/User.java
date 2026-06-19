package com.minerva.domain.entities.user;

import java.time.LocalDateTime;

import com.minerva.domain.constants.Role;
import com.minerva.domain.valueObject.LastName;
import com.minerva.domain.valueObject.Name;
import com.minerva.domain.valueObject.Password;
import com.minerva.domain.valueObject.UserName;
import com.minerva.domain.valueObject.id.DNI;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.Entity;

public class User extends Entity {
    private final DNI dni;
    private Name names;
    private LastName lastNames;
    private final UserName username;
    private Password password;
    private Role role;
    private final LocalDateTime registrationDate;

    public User(String dni, String names, String lastNames, String username, String password, Role role) throws DomainException {
        DNI tempDni = new DNI(dni);
        super(tempDni);
        this.dni = tempDni;
        this.names = new Name(names);
        this.lastNames = new LastName(lastNames);
        this.username = new UserName(username);
        this.password = new Password(password);
        if (role == null) {
            throw new DomainException("El ROL no puede ser nulo.");
        } else {
            this.role = role;
        }

        this.registrationDate = LocalDateTime.now();
    }

    public DNI getDni() {
        return dni;
    }

    public Name getNames() {
        return names;
    }

    public LastName getLastNames() {
        return lastNames;
    }

    public UserName getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    
}
