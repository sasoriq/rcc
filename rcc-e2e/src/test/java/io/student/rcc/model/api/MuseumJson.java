package io.student.rcc.model.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.api.MuseumEntity;

import java.util.Base64;
import java.util.UUID;

public record MuseumJson(
    @JsonProperty("id") UUID id,
    @JsonProperty("title") String title,
    @JsonProperty("description") String description,
    @JsonProperty("photo") String photo,
    @JsonProperty("geo") GeoJson geo) {

    public static MuseumJson fromEntity(MuseumEntity entity) {
        return new MuseumJson(entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getPhoto() != null ? Base64.getEncoder()
                        .encodeToString(entity.getPhoto()) : null,
                GeoJson.fromEntity(entity.getGeo()));
    }
}
