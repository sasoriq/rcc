package io.student.rcc.model.api;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.api.PaintingEntity;
import org.jspecify.annotations.Nullable;
import retrofit2.internal.EverythingIsNonNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
public record PaintingJson(
        @JsonProperty("id") UUID id,
        @JsonProperty("title") String title,
        @Nullable @JsonProperty("description") String description,
        @Nullable @JsonProperty("content") String content,
        @JsonProperty("artist") ArtistJson artist,
        @Nullable @JsonProperty("museum") MuseumJson museum,
        @Nullable @JsonIgnore TestData testData
) {

    public static PaintingJson fromEntity(PaintingEntity entity) {
        return new PaintingJson(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getContent(),
                ArtistJson.fromEntity(entity.getArtist()),
                MuseumJson.fromEntity(entity.getMuseum()),
            null
        );
    }

    public PaintingJson addTestData(@Nullable TestData testData) {
        return new PaintingJson(
            id,
            title,
            description,
            content,
            artist,
            museum,
            testData
        );
    }
}
