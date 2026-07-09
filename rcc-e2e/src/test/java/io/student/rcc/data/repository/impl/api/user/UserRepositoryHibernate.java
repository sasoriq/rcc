package io.student.rcc.data.repository.impl.api.user;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.UserEntity;
import io.student.rcc.data.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.jpa.EntityManagers.em;

public class UserRepositoryHibernate implements UserRepository {

    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = em(CFG.apiJdbcUrl());

    @Override
    public UserEntity create(UserEntity user) {
        entityManager.joinTransaction();
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(UserEntity.class, id));
    }

    @Override
    public List<UserEntity> findAll() {
        return entityManager
            .createQuery("select u from UserEntity u", UserEntity.class)
            .getResultList();
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try {
            return Optional.of(
                entityManager.createQuery("select u from UserEntity u where u.username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}

