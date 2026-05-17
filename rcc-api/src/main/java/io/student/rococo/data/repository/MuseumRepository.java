package io.student.rococo.data.repository;

import io.student.rococo.data.entity.MuseumEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MuseumRepository extends JpaRepository<MuseumEntity, UUID> {
    @NonNull
    Page<MuseumEntity> findByTitle(@NonNull String title, @NonNull Pageable pageable);

    @NonNull
    Optional<MuseumEntity> findByTitle(@NonNull String title);

    boolean existsByTitle(String title);
}
