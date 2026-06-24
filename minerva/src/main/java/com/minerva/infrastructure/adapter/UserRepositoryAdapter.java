package com.minerva.infrastructure.adapter;

import com.minerva.domain.entities.user.User;
import com.minerva.domain.entities.user.UserId;
import com.minerva.domain.repositories.UserRepository;
import com.minerva.domain.valueObject.UserName;
import com.minerva.infrastructure.persistence.entity.UserEntity;
import com.minerva.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public void save(User user) {
        jpaUserRepository.save(toEntity(user));
    }

    @Override
    public boolean existsById(UserId dni) {
        return jpaUserRepository.existsById(dni.value());
    }

    @Override
    public boolean existsByUsername(UserName username) {
        return jpaUserRepository.existsByUsername(username.value);
    }

    @Override
    public Optional<User> findById(UserId dni) {
        return jpaUserRepository.findById(dni.value()).map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(UserName username) {
        return jpaUserRepository.findByUsername(username.value).map(this::toDomain);
    }

    private User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getDni(),
                userEntity.getNames(),
                userEntity.getLastNames(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getRole(),
                userEntity.getIsActive(),
                userEntity.getRegistrationDate()
        );
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(
                user.getDni().value,
                user.getNames().value,
                user.getLastNames().value,
                user.getUsername().value,
                user.getPasswordHash().value,
                user.getRole(),
                user.isActive(),
                user.getRegistrationDate()
        );
    }
}
