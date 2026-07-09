package io.student.rcc.model.api;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.api.PaintingEntity;

import java.util.UUID;

public record PaintingJson(
        @JsonProperty("id") UUID id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("content") String content,
        @JsonProperty("artist") ArtistJson artist,
        @JsonProperty("museum") MuseumJson museum,
        @JsonIgnore TestData testData
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

    public PaintingJson addTestData(TestData testData) {
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
