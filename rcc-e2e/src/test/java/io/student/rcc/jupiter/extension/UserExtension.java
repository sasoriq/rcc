package io.student.rcc.jupiter.extension;

import com.github.javafaker.Faker;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.api.TestUser;
import io.student.rcc.model.api.UserJson;
import io.student.rcc.service.UsersClient;
import io.student.rcc.service.impl.UsersDbClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.jupiter.extension.TestMethodContextExtension.context;

@ParametersAreNonnullByDefault
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
                        UUID.randomUUID(),
                        anno.username().isBlank() ? faker.name().username() : anno.username(),
                        faker.name().firstName(),
                        faker.name().lastName(),
                        faker.internet().avatar()
                    );
                    usersClient.createUser(user, anno.password());
                    TestUser testUser = new TestUser(user, anno.password());
                    context.getStore(NAMESPACE).put(context.getUniqueId(), testUser);
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(TestUser.class);
    }

    @Override
    public TestUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return createUser().orElseThrow(() -> new ParameterResolutionException("User was not created"));
    }

    public static Optional<TestUser> createUser() {
        final ExtensionContext methodContext = context();
        return Optional.ofNullable(methodContext.getStore(NAMESPACE).get(methodContext.getUniqueId(), TestUser.class));
    }
}
