package com.minerva.domain.entities.user;

import java.time.LocalDateTime;

import com.minerva.domain.constants.Role;
import com.minerva.domain.interfaces.PasswordHasher;
import com.minerva.domain.valueObject.*;
import com.minerva.domain.valueObject.id.DNI;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.Entity;

public class User extends Entity {
    private final DNI dni;
    private Name names;
    private LastName lastNames;
    private final UserName username;
    private PasswordHash password;
    private Role role;
    private boolean isActive;
    private final LocalDateTime registrationDate;

    public User( PasswordHasher passwordHasher, String dni, String names, String lastNames, String username, String password, Role role) throws DomainException {
        DNI tempDni = new DNI(dni);
        super(tempDni);
        this.dni = tempDni;
        this.names = new Name(names);
        this.lastNames = new LastName(lastNames);
        this.username = new UserName(username);
        this.password = passwordHasher.hash(new Password(password));
        if (role == null) {
            throw new DomainException("El ROL no puede ser nulo.");
        } else {
            this.role = role;
        }
        this.isActive = true;
        this.registrationDate = LocalDateTime.now();
    }

    public User(String dni, String username, LocalDateTime registrationDate, String names, String lastNames, String password, Role role, boolean isActive) {
        DNI tempDni;
        try {
            tempDni = new DNI(dni);
            this.dni = tempDni;
            this.username = new UserName(username);
            this.names = new Name(names);
            this.lastNames = new LastName(lastNames);
            this.password = new PasswordHash(password);
            this.role = role;
            this.isActive = isActive;
            this.registrationDate = registrationDate;
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al cargar el usuario", e);
        }
        super(tempDni);
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

    public PasswordHash getPassword() {
        return password;
    }

    public UserName getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
}
