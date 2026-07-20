package io.student.rcc.service;

import io.student.rcc.model.api.PaintingJson;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface PaintingClient {
    PaintingJson createPainting(PaintingJson painting);
}
