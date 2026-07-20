package io.student.rcc.data.repository;

import io.student.rcc.data.entity.auth.AuthUserEntity;
import io.student.rcc.data.repository.impl.auth.AuthUserRepositoryHibernate;
import io.student.rcc.data.repository.impl.auth.AuthUserRepositoryJdbc;
import io.student.rcc.data.repository.impl.auth.AuthUserRepositorySpringJdbc;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface AuthUserRepository {
    static AuthUserRepository getInstance() {
        return switch (System.getProperty("repository.impl", "jpa")) {
            case "jdbc" -> new AuthUserRepositoryJdbc();
            case "spring-jdbc" -> new AuthUserRepositorySpringJdbc();
            default -> new AuthUserRepositoryHibernate();
        };
    }

    AuthUserEntity create(AuthUserEntity user);

    Optional<AuthUserEntity> findById(UUID id);

    List<AuthUserEntity> findAll();

    Optional<AuthUserEntity> findByUsername(String username);
}
