package io.student.rococo.data.repository;

import io.student.rococo.data.entity.MuseumEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MuseumRepository extends JpaRepository<MuseumEntity, UUID> {
    Page<MuseumEntity> findByTitle(String title, Pageable pageable);

    Optional<MuseumEntity> findByTitle(String title);

    boolean existsByTitle(String title);
}
