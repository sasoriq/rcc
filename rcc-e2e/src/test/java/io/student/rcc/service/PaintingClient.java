package io.student.rcc.service;

import io.student.rcc.model.api.PaintingJson;

public interface PaintingClient {
    PaintingJson createPainting(PaintingJson painting);
}
