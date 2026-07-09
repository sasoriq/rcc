package io.student.rcc.services;

import io.student.rcc.model.api.ArtistJson;

public interface ArtistClient {
    ArtistJson createArtist(ArtistJson artist);
}
