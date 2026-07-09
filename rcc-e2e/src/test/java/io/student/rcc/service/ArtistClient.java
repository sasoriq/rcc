package io.student.rcc.service;

import io.student.rcc.model.api.ArtistJson;

public interface ArtistClient {
    ArtistJson createArtist(ArtistJson artist);
}
