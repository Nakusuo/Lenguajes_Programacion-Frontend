package com.minerva.domain.repositories;

import com.minerva.domain.constants.Role;
import com.minerva.domain.entities.user.User;
import com.minerva.domain.entities.user.UserId;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    boolean existsById(UserId dni);
    Optional<User> findById(UserId dni);
    Role findRoleById(UserId dni);
}
