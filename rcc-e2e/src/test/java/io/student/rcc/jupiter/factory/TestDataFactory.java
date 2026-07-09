package io.student.rcc.jupiter.factory;

import com.github.javafaker.Faker;
import io.student.rcc.jupiter.annotation.Artist;
import io.student.rcc.jupiter.annotation.Museum;
import io.student.rcc.jupiter.annotation.Painting;
import io.student.rcc.model.api.ArtistJson;
import io.student.rcc.model.api.CountryJson;
import io.student.rcc.model.api.GeoJson;
import io.student.rcc.model.api.MuseumJson;
import io.student.rcc.model.api.PaintingJson;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestDataFactory {

    private static final Faker faker = new Faker();

    private TestDataFactory() {
    }

    public static ArtistJson artist(Artist anno) {
        return new ArtistJson(
            null,
            anno.name().isBlank() ? faker.artist().name() : anno.name(),
            anno.biography().isBlank() ? faker.lorem().paragraph() : anno.biography(),
            anno.photo().isBlank() ? base64(): anno.photo()
        );
    }

    public static MuseumJson museum(Museum anno) {
        return new MuseumJson(
            null,
            anno.title().isBlank() ? faker.book().title() + " Museum" : anno.title(),
            anno.description().isBlank() ? faker.lorem().paragraph(2) : anno.description(),
            anno.photo().isBlank() ? base64() : anno.photo(),
            new GeoJson(
                null,
                anno.geo().city().isBlank() ? faker.address().city() : anno.geo().city(),
                new CountryJson(
                    null,
                    anno.geo().country().isBlank() ? faker.address().country() : anno.geo().country()
                )
            )
        );
    }

    public static PaintingJson painting(Painting anno, ArtistJson artist, MuseumJson museum) {
        return new PaintingJson(
            null,
            anno.title().isBlank() ? faker.animal().name() : anno.title(),
            anno.description().isBlank() ? faker.lorem().paragraph(2) : anno.description(),
            anno.content().isBlank() ? faker.lorem().paragraph(2) : anno.content(),
            artist,
            museum,
            null
        );
    }

    private static String base64() {
        return Base64.getEncoder().encodeToString(
            faker.lorem().characters(100).getBytes(StandardCharsets.UTF_8)
        );
    }

}
