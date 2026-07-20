package io.student.rcc.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.api.GeoEntity;
import org.jspecify.annotations.Nullable;
import retrofit2.internal.EverythingIsNonNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
public record GeoJson(
    @JsonProperty("id") UUID id,
    @JsonProperty("city") String city,
    @JsonProperty("country") CountryJson country) {

    public static GeoJson fromEntity(GeoEntity entity) {
        return new GeoJson(
            entity.getId(),
            entity.getCity(),
            CountryJson.fromEntity(entity.getCountry()));
    }
}
