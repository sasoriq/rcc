package io.student.rcc.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.api.CountryEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
public record CountryJson(
    @JsonProperty("id") UUID id,
    @JsonProperty("name") String name) {

    public static CountryJson fromEntity(CountryEntity entity) {
        return new CountryJson(
                entity.getId(),
                entity.getName()
        );
    }
}
