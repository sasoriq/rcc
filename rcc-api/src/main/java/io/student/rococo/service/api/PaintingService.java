package io.student.rococo.service.api;

import io.student.rococo.data.entity.PaintingEntity;
import io.student.rococo.data.repository.PaintingRepository;
import io.student.rococo.model.PaintingJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static io.student.rococo.model.PaintingJson.fromEntity;

@Component
public class PaintingService {
    private final PaintingRepository paintingRepository;

    @Autowired
    public PaintingService(PaintingRepository paintingRepository) {
        this.paintingRepository = paintingRepository;
    }

    public Page<PaintingJson> allPaintings(Pageable pageable) {
        return paintingRepository.findAll(pageable).map(PaintingJson::fromEntity);
    }

    public PaintingJson paintingById(UUID id) {
        PaintingEntity paintingEntity = paintingRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No painting with that id"));

        return fromEntity(paintingEntity);
    }

    public Page<PaintingJson> paintingByTitle(String title, Pageable pageable) {
        Page<PaintingEntity> page = paintingRepository.findByTitle(title, pageable);
        if (page.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No painting with that id");
        }

        return page.map(PaintingJson::fromEntity);
    }

    public Page<PaintingJson> paintingByArtist(UUID artistId, Pageable pageable) {
        Page<PaintingEntity> page = paintingRepository.findByArtist(artistId, pageable);
        if (page.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found");
        }

        return page.map(PaintingJson::fromEntity);
    }

    public PaintingJson addPainting(PaintingJson painting) {
        if (paintingRepository.existsByTitle(painting.title())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Painting already exists");
        }

        PaintingEntity paintingEntity = painting.toEntity();

        paintingRepository.save(paintingEntity);
        return fromEntity(paintingEntity);
    }

    public PaintingJson updatePainting(PaintingJson painting) {
        PaintingEntity paintingEntity = paintingRepository.findByTitle(painting.title()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Painting not found"));
        paintingEntity.setDescription(painting.description());
        PaintingEntity updatedPainting = paintingRepository.save(paintingEntity);
        return fromEntity(updatedPainting);
    }
}
