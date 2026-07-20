package io.student.rcc.data.repository.impl.api.painting;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.PaintingEntity;
import io.student.rcc.data.repository.PaintingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.jpa.EntityManagers.em;

@ParametersAreNonnullByDefault
public class PaintingRepositoryHibernate implements PaintingRepository {

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = em(CFG.apiJdbcUrl());

    @Override
    public PaintingEntity create(PaintingEntity painting) {
        entityManager.joinTransaction();
        entityManager.persist(painting);
        return painting;
    }

    @Override
    public PaintingEntity update(PaintingEntity painting) {
        entityManager.joinTransaction();
        return entityManager.merge(painting);
    }

    @Override
    public Optional<PaintingEntity> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(PaintingEntity.class, id));
    }

    @Override
    public List<PaintingEntity> findAll() {
        return entityManager
            .createQuery("select p from PaintingEntity p", PaintingEntity.class)
            .getResultList();
    }

    @Override
    public Optional<PaintingEntity> findByTitle(String title) {
        try {
            return Optional.of(
                entityManager.createQuery("select p from PaintingEntity p where p.title = :title", PaintingEntity.class)
                    .setParameter("title", title)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void remove(PaintingEntity painting) {
        entityManager.joinTransaction();

        PaintingEntity managed = entityManager.find(PaintingEntity.class, painting.getId());
        if (managed != null) {
            entityManager.remove(managed);
        }
    }
}
