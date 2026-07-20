package io.student.rcc.service;

import io.student.rcc.model.api.MuseumJson;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface MuseumClient {
    MuseumJson createMuseum(MuseumJson museum);
}
