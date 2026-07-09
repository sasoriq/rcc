package io.student.rcc.model.api;

public record TestData(
    ArtistJson artist,
    MuseumJson museum
) {
    public TestData() {
        this(null, null);
    }
}
