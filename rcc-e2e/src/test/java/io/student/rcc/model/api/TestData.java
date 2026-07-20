package io.student.rcc.model.api;

import org.junit.jupiter.params.shadow.de.siegmar.fastcsv.util.Nullable;

public record TestData(
    @Nullable ArtistJson artist,
    @Nullable MuseumJson museum
) {
    public TestData() {
        this(null, null);
    }
}
