package io.student.rcc.services;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.PaintingEntity;
import io.student.rcc.data.repository.PaintingRepository;
import io.student.rcc.data.repository.impl.api.painting.PaintingRepositoryHibernate;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.api.PaintingJson;

public class PaintingDbClient implements PaintingClient {

    private static final Config CFG = Config.getInstance();

    private final PaintingRepository paintingRep = new PaintingRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
        CFG.apiJdbcUrl()
    );

    @Override
    public PaintingJson createPainting(PaintingJson paintingJson) {
        return xaTransactionTemplate.execute(() -> {
                PaintingEntity painting = PaintingEntity.fromJson(paintingJson);
                return PaintingJson.fromEntity(
                    paintingRep.create(painting)
                );
            }
        );
    }
}
