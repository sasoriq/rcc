package io.student.rococo.data.repository;

import io.student.rococo.data.entity.ArtistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {
    Page<ArtistEntity> findAll(Pageable pageable);

    Optional<ArtistEntity> findById(UUID id);

    Page<ArtistEntity> findByName(String name, Pageable pageable);

    Optional<ArtistEntity> findByName(String name);

    Boolean existsByName(String name);
}
