package io.student.rcc.data.repository.impl.api.user;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.UserEntity;
import io.student.rcc.data.mapper.UserEntityRowMapper;
import io.student.rcc.data.repository.UserRepository;
import io.student.rcc.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserRepositorySpringJdbc implements UserRepository {

    private static final Config CFG = Config.getInstance();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.apiJdbcUrl()));

    @Override
    public UserEntity create(UserEntity user) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO `user` (username, firstname, lastname, avatar) " +
                    "VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFirstname());
            ps.setString(3, user.getLastname());
            ps.setBytes(4, user.getAvatar());
            return ps;
        },kh);
        final UUID generatedKey = (UUID) Objects.requireNonNull(kh.getKeys()).get("id");
        user.setId(generatedKey);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return jdbcTemplate.query(
            "SELECT * FROM `user` WHERE id = ?",
            UserEntityRowMapper.instance,
            id
        ).stream().findFirst();
    }

    @Override
    public List<UserEntity> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM `user`",
            UserEntityRowMapper.instance
        );
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return jdbcTemplate.query(
            "SELECT * FROM `user` WHERE username = ?",
            UserEntityRowMapper.instance,
            username
        ).stream().findFirst();
    }
}
