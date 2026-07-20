package io.student.rcc.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.api.ArtistEntity;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Base64;
import java.util.UUID;

@ParametersAreNonnullByDefault
public record ArtistJson(
    @JsonProperty("id") UUID id,
    @JsonProperty("name") String name,
    @JsonProperty("biography") String biography,
    @Nullable @JsonProperty("photo") String photo
) {
    public static ArtistJson fromEntity(ArtistEntity entity) {
        return new ArtistJson(
            entity.getId(),
            entity.getName(),
            entity.getBiography(),
            entity.getPhoto() != null ? Base64.getEncoder().encodeToString(entity.getPhoto()) : null
        );
    }
}
