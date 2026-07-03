package io.student.rococo.model;

import io.student.rococo.data.entity.GeoEntity;

import java.util.UUID;

public record GeoJson(UUID id, String city, CountryJson country) {

    public static GeoJson fromEntity(GeoEntity entity) {
        return new GeoJson(entity.getId(),
                entity.getCity(),
                CountryJson.fromEntity(entity.getCountry()));
    }

    public GeoEntity toEntity() {
        GeoEntity entity = new GeoEntity();
        entity.setCity(city);
        entity.setCountry(country.toEntity());
        return entity;
    }
}
