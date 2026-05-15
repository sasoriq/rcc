package io.student.rococo.controller;

import io.student.rococo.model.ArtistJson;
import io.student.rococo.service.api.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/artist")
public class ArtistController {

    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public Page<ArtistJson> getAllArtists(Pageable pageable) {
        return artistService.allArtists(pageable);
    }

    @GetMapping("/{id}")
    public Optional<ArtistJson> getArtistById(@PathVariable UUID id) {
        return artistService.getArtistById(id);
    }

    @GetMapping(params = "name")
    public Page<ArtistJson> searchArtistByName(@RequestParam String name, Pageable pageable) {
        return artistService.getArtistByName(name, pageable);
    }

    @PostMapping
    public ArtistJson createArtist(@RequestBody ArtistJson artist) {
        return artistService.addArtist(artist);
    }

    @PatchMapping
    public ResponseEntity<ArtistJson> updateArtist(@RequestBody ArtistJson artist) {
        return artistService.updateArtist(artist);
    }
}
