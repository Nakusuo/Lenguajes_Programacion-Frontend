package com.minerva.application.service;

import com.minerva.domain.constants.Permission;
import com.minerva.domain.constants.Role;
import com.minerva.domain.entities.userAction.UserAction;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.Id;
import com.minerva.domain.repositories.UserRepository;
import com.minerva.domain.valueObject.id.UserName;

public abstract class Service {
    private final Role userRole;
    private final UserName userName;
    private final UserRepository userRepository;

    public Service(Role userRole, UserName userName, UserRepository userRepository) {
        if (userRole == null) throw new RuntimeException("El usuario debe tener un rol, no puede ser nulo");
        if (userName == null) throw new RuntimeException("El usuarioName no puede ser nulo");

        this.userRole = userRole;
        this.userName = userName;
        this.userRepository = userRepository;
    }
    // Como segunda barrera de defensa aqui tambien se deberia validar si el rol del usuario tiene permiso para ejecer la accion
    // caso contrario se lanzaria runtime exepction (decision para ti del futuro), no lo puse por pereza xd
    protected void registerUserAction(Permission permission, Id<?> entityId) {
        try {
            userRepository.save(new UserAction(userName, permission, entityId));
        } catch (DomainException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Role getUserRole() {
        return userRole;
    }
}
