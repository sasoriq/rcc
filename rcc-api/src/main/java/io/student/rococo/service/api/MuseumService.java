package io.student.rococo.service.api;

import io.student.rococo.data.entity.MuseumEntity;
import io.student.rococo.data.repository.MuseumRepository;
import io.student.rococo.model.MuseumJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static io.student.rococo.model.MuseumJson.fromEntity;

@Service
public class MuseumService {

    private final MuseumRepository museumRepository;

    @Autowired
    public MuseumService(MuseumRepository museumRepository) {
        this.museumRepository = museumRepository;
    }

    @Transactional(readOnly = true)
    public Optional<MuseumJson> museumById(UUID id) {
         MuseumEntity museumEntity = museumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No museum with the id"));
         return Optional.of(fromEntity(museumEntity));
    }

    @Transactional(readOnly = true)
    public Page<MuseumJson> allMuseums(Pageable pageable) {
        return museumRepository.findAll(pageable).map(MuseumJson::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<MuseumJson> museumByTitle(String title, Pageable pageable) {
        Page<MuseumEntity> page = museumRepository.findByTitle(title,
                pageable);
        if (page.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No museum with the title");
        }
        return page.map(MuseumJson::fromEntity);
    }

    @Transactional
    public MuseumJson addMuseum(MuseumJson museum) {
        if (museumRepository.existsByTitle(museum.title())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Museum with that title already exists");
        }

        MuseumEntity museumEntity = museum.toEntity();
        museumRepository.save(museumEntity);

        return fromEntity(museumEntity);
    }

    @Transactional
    public MuseumJson updateMuseum(MuseumJson museum) {
        MuseumEntity museumEntity = museumRepository.findByTitle(museum.title()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "The museum not found")
        );
        museumEntity.setDescription(museum.description());
        MuseumEntity updatedMuseum = museumRepository.save(museumEntity);
        return fromEntity(updatedMuseum);
    }
}
