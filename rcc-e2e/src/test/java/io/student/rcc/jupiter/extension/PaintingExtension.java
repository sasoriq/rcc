package io.student.rcc.jupiter.extension;

import io.student.rcc.jupiter.annotation.Painting;
import io.student.rcc.model.api.TestData;
import io.student.rcc.model.api.ArtistJson;
import io.student.rcc.model.api.MuseumJson;
import io.student.rcc.model.api.PaintingJson;
import io.student.rcc.service.ArtistClient;
import io.student.rcc.service.impl.ArtistDbClient;
import io.student.rcc.service.MuseumClient;
import io.student.rcc.service.impl.MuseumDbClient;
import io.student.rcc.service.PaintingClient;
import io.student.rcc.service.impl.PaintingDbClient;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static io.student.rcc.jupiter.extension.ArtistExtension.createdArtist;
import static io.student.rcc.jupiter.extension.MuseumExtension.createdMuseum;
import static io.student.rcc.jupiter.extension.TestMethodContextExtension.context;
import static io.student.rcc.jupiter.factory.TestDataFactory.*;

public class PaintingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PaintingExtension.class);
    private final PaintingClient paintingClient = new PaintingDbClient();
    private final ArtistClient artistClient = new ArtistDbClient();
    private final MuseumClient museumClient = new MuseumDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                Painting.class
        ).ifPresent(
                anno -> {
                    ArtistJson artist = createdArtist().orElseGet(() ->
                        artistClient.createArtist(
                            artist(anno.artist())
                        )
                    );
                    MuseumJson museum = createdMuseum().orElseGet(() ->
                        museumClient.createMuseum(
                            museum(anno.museum())
                        )
                    );
                    PaintingJson painting = paintingClient.createPainting(painting(anno, artist, museum));
                    painting = painting.addTestData(new TestData(artist, museum));
                    context.getStore(NAMESPACE).put(context.getUniqueId(), painting);
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(PaintingJson.class);
    }

    @Override
    public @Nullable PaintingJson resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return createdPainting().orElseThrow(() -> new ParameterResolutionException("Painting was not created"));
    }

    public static Optional<PaintingJson> createdPainting() {
        final ExtensionContext methodContext = context();
        return Optional.ofNullable(methodContext.getStore(NAMESPACE).get(methodContext.getUniqueId(), PaintingJson.class));
    }
}
