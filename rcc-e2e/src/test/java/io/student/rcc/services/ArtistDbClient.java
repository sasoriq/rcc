package io.student.rcc.services;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.ArtistEntity;
import io.student.rcc.data.repository.ArtistRepository;
import io.student.rcc.data.repository.impl.api.artist.ArtistRepositoryHibernate;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.api.ArtistJson;

public class ArtistDbClient implements ArtistClient {

    private static final Config CFG = Config.getInstance();

    private final ArtistRepository artistRep = new ArtistRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
        CFG.apiJdbcUrl()
    );

    @Override
    public ArtistJson createArtist(ArtistJson artistJson) {
        return xaTransactionTemplate.execute(() -> {
                ArtistEntity artist = ArtistEntity.fromJson(artistJson);
                return ArtistJson.fromEntity(
                    artistRep.create(artist)
                );
            }
        );
    }
}
