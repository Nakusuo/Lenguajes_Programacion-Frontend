package com.minerva.domain.interfaces;

import com.minerva.domain.valueObject.Password;
import com.minerva.domain.valueObject.PasswordHash;

public interface PasswordHasher {
    PasswordHash hash(Password rawPassword);
    boolean matches(String password, PasswordHash hashedPassword);
}
