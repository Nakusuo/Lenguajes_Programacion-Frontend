package com.minerva.domain.repositories;

import com.minerva.domain.constants.Role;
import com.minerva.domain.entities.user.User;
import com.minerva.domain.valueObject.id.DNI;

public interface UserRepository {
    void save(User user);

    boolean existsById(DNI dni);
    User findById(DNI dni);
    Role findRoleByDni(DNI dni);
}
