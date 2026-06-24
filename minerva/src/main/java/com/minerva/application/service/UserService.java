package com.minerva.application.service;

import java.util.Optional;

import com.minerva.domain.constants.Role;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.entities.user.User;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.PasswordHasher;
import com.minerva.domain.repositories.UserRepository;
import com.minerva.domain.valueObject.UserName;

public class UserService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    // --------------------- WRITE ---------------------
    public Result<Void> register(String dni, String names, String lastNames, String username, String password, Role role) {
        User userCreated;
        try {
            userCreated = new User(passwordHasher, dni, names, lastNames, username, password, role);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }

        if (userRepository.existsById(userCreated.getDni()))
            return Result.fail("Ya existe un usuario con el mismo DNI.");

        if (userRepository.existsByUsername(userCreated.getUsername()))
            return Result.fail("Ya existe un usuario con el mismo nombre de usuario.");

        userRepository.save(userCreated);
        return Result.success(null);
    }

    // aqui devolveria un token de autenticacion, pero por simplicidad devolvere un Result<Void>
    public Result<Role> authenticate(String username, String password) {
        UserName userName;
        try {
            userName = new UserName(username);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }

        Optional<User> userOptional = userRepository.findByUsername(userName);

        if (userOptional.isEmpty())
            return Result.fail("Credenciales invalidas");

        User user = userOptional.get();

        if (!user.isActive())
            return Result.fail("Credenciales invalidas");

        if (passwordHasher.matches(password, user.getPasswordHash()))
            return Result.success(user.getRole());
        else
            return Result.fail("Credenciales invalidas");
    }

}
