package com.minerva.domain.interfaces;

import com.minerva.domain.valueObject.PasswordHash;

public interface PasswordHasher {
    PasswordHash hash(String rawPassword);
    boolean matches(String password);
}
