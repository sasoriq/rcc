package io.student.rcc.model.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.api.MuseumEntity;
import org.jspecify.annotations.Nullable;
import retrofit2.internal.EverythingIsNonNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Base64;
import java.util.UUID;

@ParametersAreNonnullByDefault
public record MuseumJson(
    @JsonProperty("id") UUID id,
    @JsonProperty("title") String title,
    @Nullable @JsonProperty("description") String description,
    @Nullable @JsonProperty("photo") String photo,
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
