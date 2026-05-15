package io.student.rococo.data.repository;

import io.student.rococo.data.entity.PaintingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaintingRepository extends JpaRepository<PaintingEntity, UUID> {
    Page<PaintingEntity> findByTitle(String title, Pageable pageable);

    Optional<PaintingEntity> findByTitle(String title);

    boolean existsByTitle(String title);

    Page<PaintingEntity> findByArtist(UUID artistId, Pageable pageable);
}
