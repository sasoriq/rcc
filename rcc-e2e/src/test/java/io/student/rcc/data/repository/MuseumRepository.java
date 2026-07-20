package io.student.rcc.data.repository;

import io.student.rcc.data.entity.api.MuseumEntity;
import io.student.rcc.data.repository.impl.api.museum.MuseumRepositoryHibernate;
import io.student.rcc.data.repository.impl.api.museum.MuseumRepositoryJdbc;
import io.student.rcc.data.repository.impl.api.museum.MuseumRepositorySpringJdbc;
import retrofit2.internal.EverythingIsNonNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface MuseumRepository {
    static MuseumRepository getInstance() {
        return switch (System.getProperty("repository.impl", "jpa")) {
            case "jdbc" -> new MuseumRepositoryJdbc();
            case "spring-jdbc" -> new MuseumRepositorySpringJdbc();
            default -> new MuseumRepositoryHibernate();
        };
    }

    MuseumEntity create(MuseumEntity museum);

    MuseumEntity update(MuseumEntity museum);

    Optional<MuseumEntity> findById(UUID id);

    List<MuseumEntity> findAll();

    Optional<MuseumEntity> findByTitle(String title);

    void remove(MuseumEntity museum);
}
