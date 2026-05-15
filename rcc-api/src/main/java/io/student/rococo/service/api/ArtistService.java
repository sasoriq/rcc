package io.student.rococo.service.api;

import io.student.rococo.data.entity.ArtistEntity;
import io.student.rococo.data.repository.ArtistRepository;
import io.student.rococo.model.ArtistJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

import static io.student.rococo.model.ArtistJson.fromEntity;

@Component
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Page<ArtistJson> allArtists(Pageable pageable) {
        return artistRepository.findAll(pageable)
                .map(ArtistJson::fromEntity);
    }

    public Optional<ArtistJson> getArtistById(UUID id) {
        ArtistEntity artistEntity = artistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found"));
        return Optional.of(fromEntity(artistEntity));
    }

    public Page<ArtistJson> getArtistByName(String name, Pageable pageable) {
        Page<ArtistEntity> page = artistRepository.findByName(name, pageable);
        if (page.isEmpty()) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found"); }

        return page.map(ArtistJson::fromEntity);
    }

    public ArtistJson addArtist(ArtistJson artist) {
        if (artistRepository.existsByName(artist.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Artist already exists");
        }
        ArtistEntity artistEntity = artist.toEntity();
        artistRepository.save(artistEntity);

        return fromEntity(artistEntity);
    }

    public ResponseEntity<ArtistJson> updateArtist(ArtistJson artist) {
        ArtistEntity artistEntity = artistRepository.findByName(artist.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artist not found"));

        artistEntity.setBiography(artist.biography());
        ArtistEntity updatedArtist = artistRepository.save(artistEntity);

        return ResponseEntity.ok(fromEntity(updatedArtist));
    }
}
