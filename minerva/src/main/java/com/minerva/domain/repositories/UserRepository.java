package com.minerva.domain.repositories;

import com.minerva.domain.entities.user.User;
import com.minerva.domain.entities.user.UserId;
import com.minerva.domain.valueObject.UserName;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    boolean existsById(UserId dni);
    boolean existsByUsername(UserName username);
    Optional<User> findById(UserId dni);
    Optional<User> findByUsername(UserName username);
}
