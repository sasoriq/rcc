package io.student.rcc.data.repository.impl.api.painting;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.PaintingEntity;
import io.student.rcc.data.mapper.PaintingEntityRowMapper;
import io.student.rcc.data.repository.PaintingRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.tpl.Connections.holder;

public class PaintingRepositoryJdbc implements PaintingRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public PaintingEntity create(PaintingEntity painting) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
            "INSERT INTO painting (title, description, content, artist_id, museum_id) " +
                "VALUES (?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, painting.getTitle());
            ps.setString(2, painting.getDescription());
            ps.setString(3, painting.getContent());
            ps.setObject(4, painting.getArtist().getId());
            ps.setObject(5, painting.getMuseum().getId());
            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            painting.setId(generatedKey);
            return painting;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaintingEntity update(PaintingEntity painting) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
            "UPDATE painting SET title = ?, description = ?, content = ?, artist_id = ?, museum_id = ? " +
                "WHERE id = ?"
        )) {
            ps.setString(1, painting.getTitle());
            ps.setString(2, painting.getDescription());
            ps.setString(3, painting.getContent());
            ps.setObject(4, painting.getArtist().getId());
            ps.setObject(5, painting.getMuseum().getId());
            ps.setObject(6, painting.getId());
            ps.executeUpdate();

            return painting;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<PaintingEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
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
                "WHERE p.id = ?"
        )) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PaintingEntity entity = PaintingEntityRowMapper.instance.mapRow(rs, 1);
                    return Optional.ofNullable(entity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PaintingEntity> findAll() {
        List<PaintingEntity> paintings = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
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
                "LEFT JOIN country c ON g.country_id = c.id "
        )) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaintingEntity entity = PaintingEntityRowMapper.instance.mapRow(rs, 1);
                    paintings.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return paintings;
    }

    @Override
    public Optional<PaintingEntity> findByTitle(String title) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
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
                "WHERE p.title = ?"
        )) {
            ps.setString(1, title);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PaintingEntity entity = PaintingEntityRowMapper.instance.mapRow(rs, 1);
                    return Optional.ofNullable(entity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(PaintingEntity painting) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
            "DELETE FROM painting WHERE id = ?"
        )) {
            ps.setObject(1, painting.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
