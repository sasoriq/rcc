package io.student.rococo.model;

import io.student.rococo.data.entity.PaintingEntity;

import java.util.UUID;

public record PaintingJson(
        UUID id,
        String title,
        String description,
        String content,
        ArtistJson artist,
        MuseumJson museum
) {
    public static PaintingJson fromEntity(PaintingEntity entity) {
        return new PaintingJson(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getContent(),
                ArtistJson.fromEntity(entity.getArtist()),
                MuseumJson.fromEntity(entity.getMuseum())
        );
    }

    public PaintingEntity toEntity() {
        PaintingEntity entity = new PaintingEntity();
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setContent(content);
        entity.setArtist(artist.toEntity());
        entity.setMuseum(museum.toEntity());
        return entity;
    }
}
