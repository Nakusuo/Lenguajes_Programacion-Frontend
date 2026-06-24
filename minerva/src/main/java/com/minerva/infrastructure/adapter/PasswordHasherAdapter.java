package com.minerva.infrastructure.adapter;

import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.PasswordHasher;
import com.minerva.domain.valueObject.Password;
import com.minerva.domain.valueObject.PasswordHash;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHasherAdapter implements PasswordHasher {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public PasswordHash hash(Password rawPassword) {
        if (rawPassword == null || rawPassword.value.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        String hashed = passwordEncoder.encode(rawPassword.value);
        try {
            return new PasswordHash(hashed);
        } catch (DomainException e) {
            throw new UnexpectedDomainException(e.getMessage(), e);
        }
    }

    @Override
    public boolean matches(String password, PasswordHash hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword.value);
    }
}
