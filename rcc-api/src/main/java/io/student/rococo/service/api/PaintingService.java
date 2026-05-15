package io.student.rococo.service.api;

import io.student.rococo.data.entity.ArtistEntity;
import io.student.rococo.data.entity.MuseumEntity;
import io.student.rococo.data.entity.PaintingEntity;
import io.student.rococo.data.repository.PaintingRepository;
import io.student.rococo.model.ArtistJson;
import io.student.rococo.model.MuseumJson;
import io.student.rococo.model.PaintingJson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
public class PaintingService {
    private final PaintingRepository paintingRepository;

    public PaintingService(PaintingRepository paintingRepository) {
        this.paintingRepository = paintingRepository;
    }

    public Page<PaintingJson> allPaintings(Pageable pageable) {
        return paintingRepository.findAll(pageable).map(paintingEntity ->
               new PaintingJson(
                       paintingEntity.getId(),
                       paintingEntity.getTitle(),
                       paintingEntity.getDescription(),
                       paintingEntity.getContent(),
                       new ArtistJson(
                               paintingEntity.getArtist().getId(),
                               paintingEntity.getArtist().getName(),
                               paintingEntity.getArtist().getBiography(),
                               paintingEntity.getArtist().getPhoto()
                       ),
                       new MuseumJson(
                               paintingEntity.getMuseum().getId(),
                               paintingEntity.getMuseum().getTitle(),
                               paintingEntity.getMuseum().getDescription(),
                               paintingEntity.getMuseum().getPhoto()
                       )
               )
        );
    }

    public PaintingJson paintingById(UUID id) {
        PaintingEntity paintingEntity = paintingRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No painting with that id"));

        return new PaintingJson(
                        paintingEntity.getId(),
                        paintingEntity.getTitle(),
                        paintingEntity.getDescription(),
                        paintingEntity.getContent(),
                        new ArtistJson(
                                paintingEntity.getArtist().getId(),
                                paintingEntity.getArtist().getName(),
                                paintingEntity.getArtist().getBiography(),
                                paintingEntity.getArtist().getPhoto()
                        ),
                        new MuseumJson(
                                paintingEntity.getMuseum().getId(),
                                paintingEntity.getMuseum().getTitle(),
                                paintingEntity.getMuseum().getDescription(),
                                paintingEntity.getMuseum().getPhoto()
                        )
                );
    }

    public Page<PaintingJson> paintingByTitle(String title, Pageable pageable) {
        Page<PaintingEntity> page = paintingRepository.findByTitle(title, pageable);
        if (page.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No painting with that id");
        }

        return page.map(paintingEntity ->
                new PaintingJson(
                        paintingEntity.getId(),
                        paintingEntity.getTitle(),
                        paintingEntity.getDescription(),
                        paintingEntity.getContent(),
                        new ArtistJson(
                                paintingEntity.getArtist().getId(),
                                paintingEntity.getArtist().getName(),
                                paintingEntity.getArtist().getBiography(),
                                paintingEntity.getArtist().getPhoto()
                        ),
                        new MuseumJson(
                                paintingEntity.getMuseum().getId(),
                                paintingEntity.getMuseum().getTitle(),
                                paintingEntity.getMuseum().getDescription(),
                                paintingEntity.getMuseum().getPhoto()
                        )
                )
        );
    }

    public Page<PaintingJson> paintingByArtist(UUID artistId, Pageable pageable) {
        Page<PaintingEntity> page = paintingRepository.findByArtist(artistId, pageable);
        if (page.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found");
        }

        return page.map(paintingEntity ->
                new PaintingJson(
                        paintingEntity.getId(),
                        paintingEntity.getTitle(),
                        paintingEntity.getDescription(),
                        paintingEntity.getContent(),
                        new ArtistJson(
                                paintingEntity.getArtist().getId(),
                                paintingEntity.getArtist().getName(),
                                paintingEntity.getArtist().getBiography(),
                                paintingEntity.getArtist().getPhoto()
                        ),
                        new MuseumJson(
                                paintingEntity.getMuseum().getId(),
                                paintingEntity.getMuseum().getTitle(),
                                paintingEntity.getMuseum().getDescription(),
                                paintingEntity.getMuseum().getPhoto()
                        )
                ));
    }

    public PaintingJson addPainting(PaintingJson painting) {
        if (paintingRepository.existsByTitle(painting.title())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Painting already exists");
        }
        ArtistEntity artistEntity = new ArtistEntity();
        artistEntity.setName(painting.artist().name());
        artistEntity.setBiography(painting.artist().biography());
        artistEntity.setPhoto(painting.artist().photo());

        MuseumEntity museumEntity = new MuseumEntity();
        museumEntity.setTitle(painting.museum().title());
        museumEntity.setDescription(painting.museum().description());
        museumEntity.setPhoto(painting.museum().photo());

        PaintingEntity paintingEntity = new PaintingEntity();
        paintingEntity.setTitle(painting.title());
        paintingEntity.setDescription(painting.description());
        paintingEntity.setContent(painting.content());
        paintingEntity.setArtist(artistEntity);
        paintingEntity.setMuseum(museumEntity);

        paintingRepository.save(paintingEntity);
        return new PaintingJson(
                paintingEntity.getId(),
                paintingEntity.getTitle(),
                paintingEntity.getDescription(),
                paintingEntity.getContent(),
                new ArtistJson(
                        paintingEntity.getArtist().getId(),
                        paintingEntity.getArtist().getName(),
                        paintingEntity.getArtist().getBiography(),
                        paintingEntity.getArtist().getPhoto()
                ),
                new MuseumJson(
                        paintingEntity.getMuseum().getId(),
                        paintingEntity.getMuseum().getTitle(),
                        paintingEntity.getMuseum().getDescription(),
                        paintingEntity.getMuseum().getPhoto()
                )
        );
    }

    public PaintingJson updatePainting(PaintingJson painting) {
        PaintingEntity paintingEntity = paintingRepository.findByTitle(painting.title()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Painting not found"));
        paintingEntity.setDescription(painting.description());
        PaintingEntity updatedPainting = paintingRepository.save(paintingEntity);
        return new PaintingJson(
                updatedPainting.getId(),
                updatedPainting.getTitle(),
                updatedPainting.getDescription(),
                updatedPainting.getContent(),
                new ArtistJson(
                        updatedPainting.getArtist().getId(),
                        updatedPainting.getArtist().getName(),
                        updatedPainting.getArtist().getBiography(),
                        updatedPainting.getArtist().getPhoto()
                ),
                new MuseumJson(
                        updatedPainting.getMuseum().getId(),
                        updatedPainting.getMuseum().getTitle(),
                        updatedPainting.getMuseum().getDescription(),
                        updatedPainting.getMuseum().getPhoto()
                )
        );
    }
}
