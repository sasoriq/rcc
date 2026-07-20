package io.student.rcc.data.repository.impl.api.painting;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.PaintingEntity;
import io.student.rcc.data.mapper.PaintingEntityRowMapper;
import io.student.rcc.data.repository.PaintingRepository;
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
public class PaintingRepositorySpringJdbc implements PaintingRepository {

    private static final Config CFG = Config.getInstance();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.apiJdbcUrl()));

    @Override
    public PaintingEntity create(PaintingEntity painting) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO painting (title, description, content, artist_id, museum_id) " +
                    "VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, painting.getTitle());
            ps.setString(2, painting.getDescription());
            ps.setString(3, painting.getContent());
            ps.setObject(4, painting.getArtist().getId());
            ps.setObject(5, painting.getMuseum().getId());
            return ps;
        },kh);
        final UUID generatedKey = (UUID) Objects.requireNonNull(kh.getKeys()).get("id");
        painting.setId(generatedKey);
        return painting;
    }

    @Override
    public PaintingEntity update(PaintingEntity painting) {
        jdbcTemplate.update(
            "UPDATE painting SET title = ?, description = ?, content = ?, artist_id = ?, museum_id = ? " +
                "WHERE id = ?",
            painting.getTitle(),
            painting.getDescription(),
            painting.getContent(),
            painting.getArtist().getId(),
            painting.getMuseum().getId(),
            painting.getId()
        );
        return painting;
    }

    @Override
    public Optional<PaintingEntity> findById(UUID id) {
        return jdbcTemplate.query(
            "SELECT " +
                "p.id          AS painting_id, " +
                "p.title       AS p_title, " +
                "p.description AS p_description, " +
                "p.content     AS p_content, " +
                "a.id          AS artist_id, " +
                "a.name        AS a_name, " +
                "a.biography   AS a_biography, " +
                "a.photo       AS a_photo, " +
                "m.id          AS museum_id, " +
                "m.title       AS m_title, " +
                "m.description AS m_description, " +
                "m.photo       AS m_photo, " +
                "g.id          AS geo_id, " +
                "g.city        AS g_city, " +
                "c.id          AS country_id, " +
                "c.name        AS c_name " +
                "FROM painting p " +
                "LEFT JOIN artist a ON p.artist_id = a.id " +
                "LEFT JOIN museum m ON p.museum_id = m.id " +
                "LEFT JOIN geo g ON m.geo_id = g.id " +
                "LEFT JOIN country c ON g.country_id = c.id " +
                "WHERE p.id = ?",
            PaintingEntityRowMapper.instance,
            id
        ).stream().findFirst();
    }

    @Override
    public List<PaintingEntity> findAll() {
        return jdbcTemplate.query(
            "SELECT " +
                "p.id          AS painting_id, " +
                "p.title       AS p_title, " +
                "p.description AS p_description, " +
                "p.content     AS p_content, " +
                "a.id          AS artist_id, " +
                "a.name        AS a_name, " +
                "a.biography   AS a_biography, " +
                "a.photo       AS a_photo, " +
                "m.id          AS museum_id, " +
                "m.title       AS m_title, " +
                "m.description AS m_description, " +
                "m.photo       AS m_photo, " +
                "g.id          AS geo_id, " +
                "g.city        AS g_city, " +
                "c.id          AS country_id, " +
                "c.name        AS c_name " +
                "FROM painting p " +
                "LEFT JOIN artist a ON p.artist_id = a.id " +
                "LEFT JOIN museum m ON p.museum_id = m.id " +
                "LEFT JOIN geo g ON m.geo_id = g.id " +
                "LEFT JOIN country c ON g.country_id = c.id ",
            PaintingEntityRowMapper.instance
        );
    }

    @Override
    public Optional<PaintingEntity> findByTitle(String title) {
        return jdbcTemplate.query(
            "SELECT " +
                "p.id          AS painting_id, " +
                "p.title       AS p_title, " +
                "p.description AS p_description, " +
                "p.content     AS p_content, " +
                "a.id          AS artist_id, " +
                "a.name        AS a_name, " +
                "a.biography   AS a_biography, " +
                "a.photo       AS a_photo, " +
                "m.id          AS museum_id, " +
                "m.title       AS m_title, " +
                "m.description AS m_description, " +
                "m.photo       AS m_photo, " +
                "g.id          AS geo_id, " +
                "g.city        AS g_city, " +
                "c.id          AS country_id, " +
                "c.name        AS c_name " +
                "FROM painting p " +
                "LEFT JOIN artist a ON p.artist_id = a.id " +
                "LEFT JOIN museum m ON p.museum_id = m.id " +
                "LEFT JOIN geo g ON m.geo_id = g.id " +
                "LEFT JOIN country c ON g.country_id = c.id " +
                "WHERE p.title = ?",
            PaintingEntityRowMapper.instance,
            title
        ).stream().findFirst();
    }

    @Override
    public void remove(PaintingEntity painting) {
        jdbcTemplate.update(
            "DELETE FROM painting WHERE id = ?",
            painting.getId()
        );
    }
}
