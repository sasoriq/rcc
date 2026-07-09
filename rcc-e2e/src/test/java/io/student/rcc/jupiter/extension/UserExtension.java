package io.student.rcc.jupiter.extension;

import com.github.javafaker.Faker;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.api.UserJson;
import io.student.rcc.service.UsersClient;
import io.student.rcc.service.impl.UsersDbClient;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private final UsersClient usersClient = new UsersDbClient();
    private static final Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                User.class
        ).ifPresent(
                anno -> {
                    UserJson user = new UserJson(
                            null,
                            faker.name().username(),
                            faker.name().firstName(),
                            anno.password(),
                            faker.internet().avatar()
                    );
                    context.getStore(NAMESPACE).put(context.getUniqueId(), usersClient.createUser(user));
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(UserJson.class);
    }

    @Override
    public @Nullable Object resolveParameter(@NonNull ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
    }
}
