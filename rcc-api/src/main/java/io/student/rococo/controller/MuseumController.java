package io.student.rococo.controller;

import io.student.rococo.model.MuseumJson;
import io.student.rococo.service.api.MuseumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/museum")
public class MuseumController {

    private final MuseumService museumService;

    @Autowired
    public MuseumController(MuseumService museumService) {
        this.museumService = museumService;
    }

    @GetMapping("/{id}")
    public Optional<MuseumJson> getMuseumById(@PathVariable UUID id) {
        return museumService.museumById(id);
    }

    @GetMapping
    public Page<MuseumJson> getAllMuseums(Pageable pageable) {
        return museumService.allMuseums(pageable);
    }

    @GetMapping(params = "title")
    public Page<MuseumJson> searchMuseumByTitle(@RequestParam String title, Pageable pageable) {
        return museumService.museumByTitle(title, pageable);
    }

    @PostMapping
    public MuseumJson createMuseum(@RequestBody MuseumJson museum, @AuthenticationPrincipal Jwt principal) {
        return museumService.addMuseum(museum);
    }

    @PatchMapping
    public MuseumJson updateMuseum(@RequestBody MuseumJson museum, @AuthenticationPrincipal Jwt principal) {
        return museumService.updateMuseum(museum);
    }
}
