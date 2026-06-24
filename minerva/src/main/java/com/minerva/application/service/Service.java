package com.minerva.application.service;

import com.minerva.domain.constants.Role;

public abstract class Service {
    private final Role roleOfUser;

    public Service(Role roleOfUser) {
        if (roleOfUser == null) {
            throw new RuntimeException("El usuario debe tener un rol, no puede ser nulo");
        }
        this.roleOfUser = roleOfUser;
    }

    public Role getRoleOfUser() {
        return roleOfUser;
    }
}
