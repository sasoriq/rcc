package io.student.rcc.jupiter.extension;

import com.github.javafaker.Faker;
import io.student.rcc.jupiter.annotation.Artist;
import io.student.rcc.model.api.ArtistJson;
import io.student.rcc.services.ArtistClient;
import io.student.rcc.services.ArtistDbClient;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static io.student.rcc.jupiter.extension.TestMethodContextExtension.context;
import static io.student.rcc.jupiter.factory.TestDataFactory.artist;

public class ArtistExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ArtistExtension.class);
    private final ArtistClient artistClient = new ArtistDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                Artist.class
        ).ifPresent(
                anno -> {
                    ArtistJson artist = artist(anno);
                    context.getStore(NAMESPACE).put(context.getUniqueId(),
                        artistClient.createArtist(artist));
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(ArtistJson.class);
    }

    @Override
    public @Nullable ArtistJson resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return createdArtist().orElseThrow(() -> new ParameterResolutionException("Artist was not created"));
    }

    public static Optional<ArtistJson> createdArtist() {
        final ExtensionContext methodContext = context();
        return Optional.ofNullable(methodContext.getStore(NAMESPACE).get(methodContext.getUniqueId(), ArtistJson.class));
    }
}
