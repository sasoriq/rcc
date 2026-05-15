package io.student.rococo.controller;

import io.student.rococo.model.PaintingJson;
import io.student.rococo.service.api.PaintingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/painting")
public class PaintingController {

    private final PaintingService paintingService;

    public PaintingController(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    @GetMapping("/{id}")
    public PaintingJson getPaintingById(UUID id) {
        return paintingService.paintingById(id);
    }

    @GetMapping
    public Page<PaintingJson> getAllPaintings(Pageable pageable) {
        return paintingService.allPaintings(pageable);
    }

    @GetMapping("/author/{artistId}")
    public Page<PaintingJson> getPaintingsByArtist(@PathVariable UUID artistId, Pageable pageable) {
        return paintingService.paintingByArtist(artistId, pageable);
    }

    @GetMapping(params = "title")
    public Page<PaintingJson> getPaintingsByTitle(@RequestParam String title, Pageable pageable) {
        return paintingService.paintingByTitle(title, pageable);
    }

    @PostMapping
    public PaintingJson createPainting(@RequestBody PaintingJson painting, @AuthenticationPrincipal Jwt principal) {
        return paintingService.addPainting(painting);
    }

    @PatchMapping
    public PaintingJson updatePainting(@RequestBody PaintingJson painting, @AuthenticationPrincipal Jwt principal) {
        return paintingService.updatePainting(painting);
    }
}
