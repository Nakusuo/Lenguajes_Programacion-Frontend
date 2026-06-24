package com.minerva.infrastructure.adapter;

import com.minerva.domain.entities.user.User;
import com.minerva.domain.entities.user.UserId;
import com.minerva.domain.entities.userAction.UserAction;
import com.minerva.domain.repositories.UserRepository;
import com.minerva.domain.valueObject.DNI;
import com.minerva.infrastructure.persistence.entity.UserActionEntity;
import com.minerva.infrastructure.persistence.entity.UserEntity;
import com.minerva.infrastructure.persistence.repository.JpaUserActionRepository;
import com.minerva.infrastructure.persistence.repository.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final JpaUserRepository jpaUserRepository;
    private final JpaUserActionRepository jpaUserActionRepository;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository, JpaUserActionRepository jpaUserActionRepository) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaUserActionRepository = jpaUserActionRepository;
    }

    @Override
    public void save(User user) {
        jpaUserRepository.save(toEntity(user));
    }

    @Override
    public void save(UserAction userAction) {
        jpaUserActionRepository.save(toEntity(userAction));
    }

    @Override
    public boolean existsById(UserId dni) {
        return jpaUserRepository.existsById(dni.value());
    }

    @Override
    public boolean existsByDNI(DNI dni) {
        return jpaUserRepository.existsByDNI(dni.value);
    }

    @Override
    public Optional<User> findById(UserId dni) {
        return jpaUserRepository.findById(dni.value()).map(this::toDomain);
    }

    private User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getDni(),
                userEntity.getNames(),
                userEntity.getLastNames(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getRole(),
                userEntity.isActive(),
                userEntity.getRegistrationDate()
        );
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(
                user.getUsername().value,
                user.getDni().value,
                user.getNames().value,
                user.getLastNames().value,
                user.getPasswordHash().value,
                user.getRole(),
                user.isActive(),
                user.getRegistrationDate()
        );
    }

    private UserActionEntity toEntity(UserAction userAction) {
        UserEntity userEntity = entityManager.getReference(UserEntity.class, userAction.getUserName());


        return new UserActionEntity(
                userAction.getId().asString(),
                userEntity,
                userAction.getPermission(),
                userAction.getEntityId().asString(),
                userAction.getRegistrationDate()
        );
    }
}
