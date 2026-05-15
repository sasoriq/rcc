package io.student.rococo.data.repository;

import io.student.rococo.data.entity.ArtistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {
    Optional<ArtistEntity> findById(UUID id);

    Page<ArtistEntity> findByName(String name, Pageable pageable);

    Optional<ArtistEntity> findByName(String name);

    Boolean existsByName(String name);
}
