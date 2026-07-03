package io.student.rococo.controller;

import io.student.rococo.model.ArtistJson;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

@RestController
@RequestMapping("/api/artist")
public class ArtistMockController extends MockController<ArtistJson> {

    @Override
    protected String getMockResourcePath() {
        return "mock/artist";
    }

    @Override
    protected Class<ArtistJson> getItemClass() {
        return ArtistJson.class;
    }

    @Override
    protected String getItemId(ArtistJson item) {
        return item.id().toString();
    }

    @Override
    protected String getResourceNotFoundMessage(String id) {
        return "Artist with id \"" + id + "\" not found";
    }

    @GetMapping
    public Page<ArtistJson> getAllArtists(Pageable pageable) {
        return getFilteredPage(artist -> true, pageable);
    }

    @GetMapping("/{id}")
    public ArtistJson getArtistById(@PathVariable UUID id) {
        return findById(id.toString());
    }

    @GetMapping(params = "name")
    public Page<ArtistJson> searchArtistByName(@RequestParam String name, Pageable pageable) {
        return getFilteredPage(artist -> artist.name()
                .equalsIgnoreCase(name), pageable);
    }

    @PostMapping
    public ArtistJson createArtist(@RequestBody ArtistJson artist) {
        return artist;
    }

    @PatchMapping
    public ResponseEntity<ArtistJson> updateArtist(@RequestBody ArtistJson artist) {
        return ResponseEntity.ok(artist);
    }
}
