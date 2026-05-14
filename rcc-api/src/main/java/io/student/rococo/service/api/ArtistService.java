package io.student.rococo.service.api;

import io.student.rococo.data.entity.ArtistEntity;
import io.student.rococo.data.repository.ArtistRepository;
import io.student.rococo.model.ArtistJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;

@Component
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Page<ArtistJson> allArtists(Pageable pageable) {
        return artistRepository.findAll(pageable)
                .map(artistEntity -> new ArtistJson(
                        artistEntity.getId(),
                        artistEntity.getName(),
                        artistEntity.getBiography(),
                        artistEntity.getPhoto()));
    }

    public Optional<ArtistJson> getArtistById(UUID id) {
        ArtistEntity artistEntity = artistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found"));
        return Optional.of(new ArtistJson(
                artistEntity.getId(),
                artistEntity.getName(),
                artistEntity.getBiography(),
                artistEntity.getPhoto()));
    }

    public Page<ArtistJson> getArtistByName(String name, Pageable pageable) {
        Page<ArtistEntity> page = artistRepository.findByName(name, pageable);
        if (page.isEmpty()) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found"); }

        return page.map(artistEntity -> new ArtistJson(
                artistEntity.getId(),
                artistEntity.getName(),
                artistEntity.getBiography(),
                artistEntity.getPhoto()));
    }

    public ArtistJson addArtist(ArtistJson artist, Jwt principal) {
        if (artistRepository.existsByName(artist.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Artist already exists");
        }

        ArtistEntity artistEntity = new ArtistEntity();
        artistEntity.setName(artist.name());
        artistEntity.setBiography(artist.biography());
        artistEntity.setPhoto(artist.photo());
        artistRepository.save(artistEntity);

        return new ArtistJson(
                artistEntity.getId(),
                artistEntity.getName(),
                artistEntity.getBiography(),
                artistEntity.getPhoto());
    }

    public ResponseEntity<ArtistJson> updateArtist(ArtistJson artist, Jwt principal) {
        ArtistEntity artistEntity = artistRepository.findByName(artist.name())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artist not found"));

        artistEntity.setBiography(artist.biography());
        ArtistEntity updatedArtist = artistRepository.save(artistEntity);
        return ResponseEntity.ok(new ArtistJson(
                updatedArtist.getId(),
                updatedArtist.getName(),
                updatedArtist.getBiography(),
                updatedArtist.getPhoto()));
    }
}
