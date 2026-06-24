package com.minerva.domain.entities.user;

import java.time.LocalDateTime;

import com.minerva.domain.constants.Role;
import com.minerva.domain.interfaces.PasswordHasher;
import com.minerva.domain.valueObject.*;
import com.minerva.domain.valueObject.DNI;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.Entity;
import com.minerva.domain.valueObject.id.UserName;

public class User extends Entity<UserId> {
    private final DNI dni;
    private Name names;
    private LastName lastNames;
    private final UserName username;
    private PasswordHash passwordHash;
    private Role role;
    private boolean isActive;
    private final LocalDateTime registrationDate;

    public User(PasswordHasher passwordHasher, String dni, String names, String lastNames, String username, String password, Role role) throws DomainException {
        UserName tempUserName = new UserName(username);
        super(tempUserName);
        this.dni = new DNI(dni);
        this.names = new Name(names);
        this.lastNames = new LastName(lastNames);
        this.username = tempUserName;
        this.passwordHash = passwordHasher.hash(new Password(password));
        if (role == null) {
            throw new DomainException("El ROL no puede ser nulo.");
        } else {
            this.role = role;
        }
        this.isActive = true;
        this.registrationDate = LocalDateTime.now();
    }

    public User(String dni, String names, String lastNames, String username, String password, Role role, boolean isActive, LocalDateTime registrationDate) {
        UserName tempUserName;
        try {
            tempUserName = new UserName(username);
            this.dni = new DNI(dni);
            this.username = tempUserName;
            this.names = new Name(names);
            this.lastNames = new LastName(lastNames);
            this.passwordHash = new PasswordHash(password);
            this.role = role;
            this.isActive = isActive;
            this.registrationDate = registrationDate;
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al cargar el usuario", e);
        }
        super(tempUserName);
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

    public PasswordHash getPasswordHash() {
        return passwordHash;
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
