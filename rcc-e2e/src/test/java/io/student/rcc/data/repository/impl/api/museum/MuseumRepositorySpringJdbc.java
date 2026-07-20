package io.student.rcc.data.repository.impl.api.museum;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.MuseumEntity;
import io.student.rcc.data.mapper.MuseumEntityRowMapper;
import io.student.rcc.data.repository.MuseumRepository;
import io.student.rcc.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class MuseumRepositorySpringJdbc implements MuseumRepository {

    private static final Config CFG = Config.getInstance();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.apiJdbcUrl()));

    @Override
    public MuseumEntity create(MuseumEntity museum) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO museum (title, description, city, photo, geo_id) " +
                    "VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, museum.getTitle());
            ps.setString(2, museum.getDescription());
            ps.setString(3, museum.getGeo().getCity());
            ps.setBytes(4, museum.getPhoto());
            ps.setObject(5, museum.getGeo().getCountry().getId());
            return ps;
        },kh);
        final UUID generatedKey = (UUID) Objects.requireNonNull(kh.getKeys()).get("id");
        museum.setId(generatedKey);
        return museum;
    }

    @Override
    public MuseumEntity update(MuseumEntity museum) {
        jdbcTemplate.update(
                "UPDATE museum SET title = ?, description = ?, city = ?, photo = ?, country_id = ? " +
                    "WHERE id = ?",
            museum.getTitle(),
            museum.getDescription(),
            museum.getGeo().getCity(),
            museum.getPhoto(),
            museum.getGeo().getCountry().getId(),
            museum.getId()
        );
        return museum;
    }

    @Override
    public Optional<MuseumEntity> findById(UUID id) {
        return jdbcTemplate.query(
            "SELECT " +
                "m.id AS museum_id, " +
                "m.title, " +
                "m.description, " +
                "m.photo, " +
                "g.id AS geo_id, " +
                "g.city, " +
                "c.id AS country_id, " +
                "c.name AS country_name " +
                "FROM museum m " +
                "JOIN geo g ON m.geo_id = g.id " +
                "JOIN country c ON g.country_id = c.id " +
                "WHERE m.id = ?",
            MuseumEntityRowMapper.instance,
            id
        ).stream().findFirst();
    }

    @Override
    public List<MuseumEntity> findAll() {
        return jdbcTemplate.query(
            "SELECT " +
                "m.id AS museum_id, " +
                "m.title, " +
                "m.description, " +
                "m.photo, " +
                "g.id AS geo_id, " +
                "g.city, " +
                "c.id AS country_id, " +
                "c.name AS country_name " +
                "FROM museum m " +
                "JOIN geo g ON m.geo_id = g.id " +
                "JOIN country c ON g.country_id = c.id",
            MuseumEntityRowMapper.instance
        );
    }

    @Override
    public Optional<MuseumEntity> findByTitle(String title) {
        return jdbcTemplate.query(
            "SELECT " +
                "m.id AS museum_id, " +
                "m.title, " +
                "m.description, " +
                "m.photo, " +
                "g.id AS geo_id, " +
                "g.city, " +
                "c.id AS country_id, " +
                "c.name AS country_name " +
                "FROM museum m " +
                "JOIN geo g ON m.geo_id = g.id " +
                "JOIN country c ON g.country_id = c.id " +
                "WHERE m.title = ?",
            MuseumEntityRowMapper.instance,
            title
        ).stream().findFirst();
    }

    @Override
    public void remove(MuseumEntity museum) {
        jdbcTemplate.update(
            "DELETE FROM museum WHERE id = ?",
            museum.getId()
        );
    }
}
