package io.student.rcc.data.repository;

import io.student.rcc.data.entity.api.UserEntity;
import io.student.rcc.data.repository.impl.api.user.UserRepositoryHibernate;
import io.student.rcc.data.repository.impl.api.user.UserRepositoryJdbc;
import io.student.rcc.data.repository.impl.api.user.UserRepositorySpringJdbc;
import retrofit2.internal.EverythingIsNonNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserRepository {
    static UserRepository getInstance() {
        return switch (System.getProperty("repository.impl", "jpa")) {
            case "jdbc" -> new UserRepositoryJdbc();
            case "spring-jdbc" -> new UserRepositorySpringJdbc();
            default -> new UserRepositoryHibernate();
        };
    }

    UserEntity create(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    List<UserEntity> findAll();

    Optional<UserEntity> findByUsername(String username);
}
