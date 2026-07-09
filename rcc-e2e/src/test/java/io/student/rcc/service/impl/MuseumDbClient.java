package io.student.rcc.service.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.MuseumEntity;
import io.student.rcc.data.repository.MuseumRepository;
import io.student.rcc.data.repository.impl.api.museum.MuseumRepositoryHibernate;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.api.MuseumJson;
import io.student.rcc.service.MuseumClient;

public class MuseumDbClient implements MuseumClient {

    private static final Config CFG = Config.getInstance();

    private final MuseumRepository museumRep = new MuseumRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
        CFG.apiJdbcUrl()
    );

    @Override
    public MuseumJson createMuseum(MuseumJson museumJson) {
        return xaTransactionTemplate.execute(() -> {
                MuseumEntity museum = MuseumEntity.fromJson(museumJson);
                return MuseumJson.fromEntity(
                    museumRep.create(museum)
                );
            }
        );
    }
}
