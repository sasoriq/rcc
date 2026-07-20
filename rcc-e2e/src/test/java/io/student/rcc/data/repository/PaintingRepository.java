package io.student.rcc.data.repository;

import io.student.rcc.data.entity.api.PaintingEntity;
import io.student.rcc.data.repository.impl.api.painting.PaintingRepositoryHibernate;
import io.student.rcc.data.repository.impl.api.painting.PaintingRepositoryJdbc;
import io.student.rcc.data.repository.impl.api.painting.PaintingRepositorySpringJdbc;
import retrofit2.internal.EverythingIsNonNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface PaintingRepository {
    static PaintingRepository getInstance() {
        return switch (System.getProperty("repository.impl", "jpa")) {
            case "jdbc" -> new PaintingRepositoryJdbc();
            case "spring-jdbc" -> new PaintingRepositorySpringJdbc();
            default -> new PaintingRepositoryHibernate();
        };
    }

    PaintingEntity create(PaintingEntity painting);

    PaintingEntity update(PaintingEntity painting);

    Optional<PaintingEntity> findById(UUID id);

    List<PaintingEntity> findAll();

    Optional<PaintingEntity> findByTitle(String title);

    void remove(PaintingEntity painting);
}
