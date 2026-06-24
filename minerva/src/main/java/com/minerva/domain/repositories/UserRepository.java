package com.minerva.domain.repositories;

import com.minerva.domain.entities.user.User;
import com.minerva.domain.entities.user.UserId;
import com.minerva.domain.entities.userAction.UserAction;
import com.minerva.domain.valueObject.DNI;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    void save(UserAction userAction);

    boolean existsById(UserId id);
    boolean existsByDNI(DNI dni);
    Optional<User> findById(UserId id);
}
