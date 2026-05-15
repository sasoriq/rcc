package io.student.rococo.model;

import io.student.rococo.data.entity.ArtistEntity;

import java.util.UUID;

public record ArtistJson(UUID id, String name, String biography, byte[] photo) {

    public static ArtistJson fromEntity(ArtistEntity entity) {
        return new ArtistJson(
                entity.getId(),
                entity.getName(),
                entity.getBiography(),
                entity.getPhoto());
    }

    public ArtistEntity toEntity() {
        ArtistEntity entity = new ArtistEntity();
        entity.setName(name);
        entity.setBiography(biography);
        entity.setPhoto(photo);
        return entity;
    }

}
