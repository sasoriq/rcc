package io.student.rcc.service.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.UserEntity;
import io.student.rcc.data.entity.auth.AuthUserEntity;
import io.student.rcc.data.entity.auth.AuthorityEntity;
import io.student.rcc.data.repository.AuthUserRepository;
import io.student.rcc.data.repository.UserRepository;
import io.student.rcc.data.repository.impl.api.user.UserRepositoryHibernate;
import io.student.rcc.data.repository.impl.auth.AuthUserRepositoryHibernate;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.api.UserJson;
import io.student.rcc.model.auth.Authority;
import io.student.rcc.service.UsersClient;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRep = new AuthUserRepositoryHibernate();
    private final UserRepository userRep = new UserRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
        CFG.authJdbcUrl(),
        CFG.apiJdbcUrl()
    );

    public UserJson createUser(String username) {
        return xaTransactionTemplate.execute(() ->
            UserJson.fromEntity(persistUser(username))
        );
    }

    @Override
    public UserJson createUser(UserJson user) {
        return xaTransactionTemplate.execute(() ->
            UserJson.fromEntity(persistUser(user.username()))
        );
    }

    private UserEntity persistUser(String username) {
        AuthUserEntity authUser = createAuthUserEntity(username);
        authUserRep.create(authUser);
        return userRep.create(createUserEntity(username));
    }

    private UserEntity createUserEntity(String username) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        return user;
    }

    private AuthUserEntity createAuthUserEntity(String username) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
            Arrays.stream(Authority.values()).map(
                authority -> {
                    AuthorityEntity userAuthority = new AuthorityEntity();
                    userAuthority.setAuthority(authority);
                    userAuthority.setUser(authUser);
                    return userAuthority;
                }
            ).toList()
        );
        return authUser;
    }
}
