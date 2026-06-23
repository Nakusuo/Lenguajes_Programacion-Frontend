package com.minerva.domain.repositories;

import com.minerva.domain.constants.Role;
import com.minerva.domain.entities.user.User;
import com.minerva.domain.entities.user.UserId;
import com.minerva.domain.valueObject.id.DNI;

public interface UserRepository {
    void save(User user);

    boolean existsById(UserId dni);
    User findById(UserId dni);
    Role findRoleByDni(UserId dni);
}
