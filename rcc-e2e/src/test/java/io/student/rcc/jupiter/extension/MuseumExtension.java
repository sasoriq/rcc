package io.student.rcc.jupiter.extension;

import io.student.rcc.jupiter.annotation.Museum;
import io.student.rcc.model.api.MuseumJson;
import io.student.rcc.service.MuseumClient;
import io.student.rcc.service.impl.MuseumDbClient;
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
import static io.student.rcc.jupiter.factory.TestDataFactory.museum;

public class MuseumExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(MuseumExtension.class);
    private final MuseumClient museumClient = new MuseumDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                Museum.class
        ).ifPresent(
                anno -> {
                    MuseumJson museum = museum(anno);
                    context.getStore(NAMESPACE).put(context.getUniqueId(),
                        museumClient.createMuseum(museum));
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(MuseumJson.class);
    }

    @Override
    public @Nullable MuseumJson resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return createdMuseum().orElseThrow(() -> new ParameterResolutionException("MuseumJson was not created"));
    }

    public static Optional<MuseumJson> createdMuseum() {
        final ExtensionContext methodContext = context();
        return Optional.ofNullable(methodContext.getStore(NAMESPACE)
            .get(methodContext.getUniqueId(), MuseumJson.class));
    }
}
