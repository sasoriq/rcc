package io.student.rcc.service;

import io.student.rcc.model.api.MuseumJson;

public interface MuseumClient {
    MuseumJson createMuseum(MuseumJson museum);
}
