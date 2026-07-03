package io.student.rococo.model;

import io.student.rococo.data.entity.MuseumEntity;

import java.util.Base64;
import java.util.UUID;

public record MuseumJson(UUID id, String title, String description, String photo, GeoJson geo) {

    public static MuseumJson fromEntity(MuseumEntity entity) {
        return new MuseumJson(entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getPhoto() != null ? Base64.getEncoder()
                        .encodeToString(entity.getPhoto()) : null,
                GeoJson.fromEntity(entity.getGeo()));
    }

    public MuseumEntity toEntity() {
        MuseumEntity entity = new MuseumEntity();
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setPhoto(photo != null ? Base64.getDecoder()
                .decode(photo) : null);
        entity.setGeo(geo().toEntity());
        return entity;
    }
}
