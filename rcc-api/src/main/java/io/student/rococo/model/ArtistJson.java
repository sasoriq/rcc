package io.student.rococo.model;

import io.student.rococo.data.entity.ArtistEntity;

import java.util.Base64;
import java.util.UUID;

public record ArtistJson(UUID id, String name, String biography, String photo) {

    public static ArtistJson fromEntity(ArtistEntity entity) {
        return new ArtistJson(
                entity.getId(),
                entity.getName(),
                entity.getBiography(),
                entity.getPhoto() != null ? Base64.getEncoder().encodeToString(entity.getPhoto()) : null
        );
    }

    public ArtistEntity toEntity() {
        ArtistEntity entity = new ArtistEntity();
        entity.setName(name);
        entity.setBiography(biography);
        entity.setPhoto(photo != null ? Base64.getDecoder().decode(photo) : null);
        return entity;
    }

}
