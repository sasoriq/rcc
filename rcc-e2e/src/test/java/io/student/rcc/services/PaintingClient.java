package io.student.rcc.services;

import io.student.rcc.model.api.PaintingJson;

public interface PaintingClient {
    PaintingJson createPainting(PaintingJson painting);
}
