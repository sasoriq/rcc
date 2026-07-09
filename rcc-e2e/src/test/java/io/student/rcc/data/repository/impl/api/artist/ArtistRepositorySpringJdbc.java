package io.student.rcc.data.repository.impl.api.artist;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.ArtistEntity;
import io.student.rcc.data.mapper.ArtistEntityRowMapper;
import io.student.rcc.data.repository.ArtistRepository;
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

public class ArtistRepositorySpringJdbc implements ArtistRepository {

    private static final Config CFG = Config.getInstance();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.apiJdbcUrl()));

    @Override
    public ArtistEntity create(ArtistEntity artist) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO artist (name, biography, photo) " +
                    "VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, artist.getName());
            ps.setString(2, artist.getBiography());
            ps.setBytes(3, artist.getPhoto());
            return ps;
        },kh);
        final UUID generatedKey = (UUID) Objects.requireNonNull(kh.getKeys()).get("id");
        artist.setId(generatedKey);
        return artist;
    }

    @Override
    public ArtistEntity update(ArtistEntity artist) {
        jdbcTemplate.update(
                "UPDATE artist SET name = ?, biography = ?, photo = ? " +
                    "WHERE id = ?",
            artist.getName(),
            artist.getBiography(),
            artist.getPhoto(),
            artist.getId()
        );
        return artist;
    }

    @Override
    public Optional<ArtistEntity> findById(UUID id) {
        return jdbcTemplate.query(
            "SELECT * FROM artist WHERE id = ?",
            ArtistEntityRowMapper.instance,
            id
        ).stream().findFirst();
    }

    @Override
    public List<ArtistEntity> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM artist",
            ArtistEntityRowMapper.instance
        );
    }

    @Override
    public Optional<ArtistEntity> findByName(String name) {
        return jdbcTemplate.query(
            "SELECT * FROM artist WHERE name = ?",
            ArtistEntityRowMapper.instance,
            name
        ).stream().findFirst();
    }

    @Override
    public void remove(ArtistEntity artist) {
        jdbcTemplate.update(
            "DELETE FROM artist WHERE id = ?",
            artist.getId()
        );
    }
}
