package io.student.rococo.model;

import io.student.rococo.data.entity.CountryEntity;

import java.util.UUID;

public record CountryJson(UUID id, String name) {

    public static CountryJson fromEntity(CountryEntity entity) {
        return new CountryJson(
                entity.getId(),
                entity.getName()
        );
    }
}
