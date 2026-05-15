package io.student.rococo.model;

import io.student.rococo.data.entity.MuseumEntity;

import java.util.UUID;

public record MuseumJson(UUID id, String title, String description, byte[] photo) {

    public static MuseumJson fromEntity(MuseumEntity entity) {
        return new MuseumJson(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getPhoto());
    }

    public MuseumEntity toEntity() {
        MuseumEntity entity = new MuseumEntity();
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setPhoto(photo);
        return entity;
    }
}
