package io.student.rococo.controller;

import io.student.rococo.model.PaintingJson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/painting")
public class PaintingMockController extends MockController<PaintingJson> {

    @Override
    protected String getMockResourcePath() {
        return "mock/painting";
    }

    @Override
    protected Class<PaintingJson> getItemClass() {
        return PaintingJson.class;
    }

    @Override
    protected String getItemId(PaintingJson item) {
        return item.id().toString();
    }

    @Override
    protected String getResourceNotFoundMessage(String id) {
        return "Painting with id \"" + id + "\" not found";
    }

    @GetMapping("/{id}")
    public PaintingJson getPaintingById(UUID id) {
        return findById(id.toString());
    }

    @GetMapping
    public Page<PaintingJson> getAllPaintings(Pageable pageable) {
        return getFilteredPage(painting -> true, pageable);
    }

    @GetMapping("/author/{artistId}")
    public Page<PaintingJson> getPaintingsByArtist(@PathVariable UUID artistId, Pageable pageable) {
        return getFilteredPage(painting -> painting.artist().id().equals(artistId), pageable);
    }

    @GetMapping(params = "title")
    public Page<PaintingJson> getPaintingsByTitle(@RequestParam String title, Pageable pageable) {
        return getFilteredPage(painting -> painting.title()
                .equalsIgnoreCase(title), pageable);
    }

    @PostMapping
    public PaintingJson createPainting(@RequestBody PaintingJson painting, @AuthenticationPrincipal Jwt principal) {
        return painting;
    }

    @PatchMapping
    public PaintingJson updatePainting(@RequestBody PaintingJson painting, @AuthenticationPrincipal Jwt principal) {
        return painting;
    }
}
