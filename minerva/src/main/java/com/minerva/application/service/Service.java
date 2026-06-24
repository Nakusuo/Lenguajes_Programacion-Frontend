package com.minerva.application.service;

import com.minerva.domain.constants.Permission;
import com.minerva.domain.constants.Role;
import com.minerva.domain.entities.userAction.UserAction;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.Entity;
import com.minerva.domain.repositories.UserRepository;

public abstract class Service {
    protected final Role userRole;
    private final String userName;
    protected final UserRepository userRepository;

    public Service(Role userRole, String userName, UserRepository userRepository) {
        if (userRole == null) throw new RuntimeException("El usuario debe tener un rol, no puede ser nulo");
        if (userName == null) throw new RuntimeException("El usuarioName no puede ser nulo");

        this.userRole = userRole;
        this.userName = userName;
        this.userRepository = userRepository;
    }

    protected void registerUserAction(Permission permission, Entity<?> entity) {
        try {
            userRepository.save(new UserAction(userName, permission, entity));
        } catch (DomainException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
