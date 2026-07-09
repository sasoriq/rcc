package io.student.rcc.services;

import io.student.rcc.model.api.MuseumJson;

public interface MuseumClient {
    MuseumJson createMuseum(MuseumJson museum);
}
