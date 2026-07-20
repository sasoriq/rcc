package io.student.rcc.data.repository.impl.api.artist;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.ArtistEntity;
import io.student.rcc.data.repository.ArtistRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.jpa.EntityManagers.em;

@ParametersAreNonnullByDefault
public class ArtistRepositoryHibernate implements ArtistRepository {

    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = em(CFG.apiJdbcUrl());

    @Override
    public ArtistEntity create(ArtistEntity artist) {
        entityManager.joinTransaction();
        entityManager.persist(artist);
        return artist;
    }

    @Override
    public ArtistEntity update(ArtistEntity artist) {
        entityManager.joinTransaction();
        return entityManager.merge(artist);
    }

    @Override
    public Optional<ArtistEntity> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(ArtistEntity.class, id));
    }

    @Override
    public List<ArtistEntity> findAll() {
        return entityManager
            .createQuery("select a from ArtistEntity a", ArtistEntity.class)
            .getResultList();
    }

    @Override
    public Optional<ArtistEntity> findByName(String name) {
        try {
            return Optional.of(
                entityManager.createQuery("select a from ArtistEntity a where a.name = :name", ArtistEntity.class)
                    .setParameter("name", name)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void remove(ArtistEntity artist) {
        entityManager.joinTransaction();

        ArtistEntity managed = entityManager.find(ArtistEntity.class, artist.getId());
        if (managed != null) {
            entityManager.remove(managed);
        }
    }
}
