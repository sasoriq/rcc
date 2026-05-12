package io.student.rcc.services;

import io.student.rcc.config.Config;
import io.student.rcc.model.Authority;
import io.student.rcc.model.UserJson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.util.UUID;

public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserJson createUser(UserJson user) {
        final String userId = UUID.randomUUID().toString();

        final JdbcTemplate jdbcTemplate = new JdbcTemplate(
                new SingleConnectionDataSource(
                        CFG.authJdbcUrl(),
                        CFG.dbUsername(),
                        CFG.dbPassword(),
                        true
                )
        );

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO user (id, username, account_non_expired, account_non_locked, " +
                                "credentials_non_expired, enabled, password) VALUES (UUID_TO_BIN(?, true), ?, ?, ?, ?, ?, ?)"
                    );
                    ps.setString(1, userId);
                    ps.setString(2, user.username());
                    ps.setInt(3, 1);
                    ps.setInt(4, 1);
                    ps.setInt(5, 1);
                    ps.setInt(6, 1);
                    ps.setString(7, passwordEncoder.encode(user.password()));
                    return ps;
                }
        );

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO authority (authority, user_id) VALUES (?, UUID_TO_BIN(?, true))"
                    );
                    ps.setString(1, Authority.read.name());
                    ps.setString(2, userId);
                    return ps;
                }
        );

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO authority (authority, user_id) VALUES (?, UUID_TO_BIN(?, true))"
                    );
                    ps.setString(1, Authority.write.name());
                    ps.setString(2, userId);
                    return ps;
                }
        );

        return new UserJson(
                UUID.fromString(userId),
                user.username(),
                user.firstname(),
                user.password(),
                user.avatar()
        );
    }
}
