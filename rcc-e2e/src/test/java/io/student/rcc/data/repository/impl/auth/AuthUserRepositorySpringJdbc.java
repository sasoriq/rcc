package io.student.rcc.data.repository.impl.auth;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.auth.AuthUserEntity;
import io.student.rcc.data.entity.auth.AuthorityEntity;
import io.student.rcc.data.repository.AuthUserRepository;
import io.student.rcc.data.tpl.DataSources;
import io.student.rcc.model.auth.Authority;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository, ResultSetExtractor<AuthUserEntity> {

    private static final Config CFG = Config.getInstance();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO `user` (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );

                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setBoolean(3, user.getEnabled());
                ps.setBoolean(4, user.getAccountNonExpired());
                ps.setBoolean(5, user.getAccountNonLocked());
                ps.setBoolean(6, user.getCredentialsNonExpired());
                return ps;
            },
            kh);
        final UUID generatedKey = (UUID) Objects.requireNonNull(kh.getKeys()).get("id");
        user.setId(generatedKey);

        jdbcTemplate.batchUpdate(
            "INSERT INTO `authority` (user_id, authority) VALUES (?, ?)",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                    ps.setObject(1, user.getId());
                    ps.setString(2, user.getAuthorities().get(i).getAuthority().name());
                }

                @Override
                public int getBatchSize() {
                    return user.getAuthorities().size();
                }
            }
        );
        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.ofNullable(jdbcTemplate.query(
            "SELECT " +
                "u.id AS u_id, " +
                "u.username AS username, " +
                "u.password AS password, " +
                "u.enabled AS enabled, " +
                "u.account_non_expired AS account_non_expired, " +
                "u.account_non_locked AS account_non_locked, " +
                "u.credentials_non_expired AS credentials_non_expired, " +
                "a.id AS a_id, " +
                "a.user_id AS a_user_id, " +
                "a.authority AS authority " +
                "FROM `user` u JOIN `authority` a ON u.id = a.user_id WHERE u.id = ?",
            this,
            id
        ));
    }

    @Override
    public List<AuthUserEntity> findAll() {
        return jdbcTemplate.query(
            "SELECT " +
                "u.id AS u_id, " +
                "u.username AS username, " +
                "u.password AS password, " +
                "u.enabled AS enabled, " +
                "u.account_non_expired AS account_non_expired, " +
                "u.account_non_locked AS account_non_locked, " +
                "u.credentials_non_expired AS credentials_non_expired, " +
                "a.id AS a_id, " +
                "a.user_id AS a_user_id, " +
                "a.authority AS authority " +
                "FROM `user` u JOIN `authority` a ON u.id = a.user_id",
            this::extractAllData
        );
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        return Optional.ofNullable(jdbcTemplate.query(
            "SELECT " +
                "u.id AS u_id, " +
                "u.username AS username, " +
                "u.password AS password, " +
                "u.enabled AS enabled, " +
                "u.account_non_expired AS account_non_expired, " +
                "u.account_non_locked AS account_non_locked, " +
                "u.credentials_non_expired AS credentials_non_expired, " +
                "a.id AS a_id, " +
                "a.user_id AS a_user_id, " +
                "a.authority AS authority " +
                "FROM `user` u JOIN `authority` a ON u.id = a.user_id WHERE u.username = ?",
            this,
            username
        ));
    }

    @Override
    public AuthUserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, AuthUserEntity> userMap = new HashMap<>();
        UUID userId = null;
        while (rs.next()) {
            userId = rs.getObject("u_id", UUID.class);
            AuthUserEntity user = userMap.computeIfAbsent(userId, id -> {
                try {
                    return extractUserEntity(rs);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            AuthorityEntity authority = extractAuthorityEntity(rs, user);
            user.getAuthorities().add(authority);
        }
        return userMap.get(userId);
    }

    private List<AuthUserEntity> extractAllData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, AuthUserEntity> userMap = new HashMap<>();
        while (rs.next()) {
            UUID userId = rs.getObject("u_id", UUID.class);
            AuthUserEntity user = userMap.computeIfAbsent(userId, id -> {
                    try {
                        return extractUserEntity(rs);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            );

            AuthorityEntity authority = extractAuthorityEntity(rs, user);
            user.getAuthorities().add(authority);
        }
        return new ArrayList<>(userMap.values());
    }

    private AuthUserEntity extractUserEntity(ResultSet rs) throws SQLException {
        AuthUserEntity entity = new AuthUserEntity();
        entity.setId(rs.getObject("u_id", UUID.class));
        entity.setUsername(rs.getString("username"));
        entity.setPassword(rs.getString("password"));
        entity.setEnabled(rs.getBoolean("enabled"));
        entity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
        entity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
        entity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        return entity;
    }

    private AuthorityEntity extractAuthorityEntity(ResultSet rs, AuthUserEntity user) throws SQLException {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setId(rs.getObject("a_id", UUID.class));
        authority.setUser(user);
        authority.setAuthority(Authority.valueOf(rs.getString("authority")));
        return authority;
    }
}
