package io.student.rcc.data.repository.impl.api.museum;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.MuseumEntity;
import io.student.rcc.data.mapper.MuseumEntityRowMapper;
import io.student.rcc.data.repository.MuseumRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.tpl.Connections.holder;

public class MuseumRepositoryJdbc implements MuseumRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public MuseumEntity create(MuseumEntity museum) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
            "INSERT INTO museum (title, description, city, photo, geo_id) " +
                "VALUES (?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, museum.getTitle());
            ps.setString(2, museum.getDescription());
            ps.setString(3, museum.getGeo().getCity());
            ps.setBytes(4, museum.getPhoto());
            ps.setObject(5, museum.getGeo().getCountry().getId());
            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            museum.setId(generatedKey);
            return museum;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MuseumEntity update(MuseumEntity museum) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
            "UPDATE museum SET title = ?, description = ?, geo_id = ?, photo = ? " +
                "WHERE id = ?"
        )) {
            ps.setString(1, museum.getTitle());
            ps.setString(2, museum.getDescription());
            ps.setObject(3, museum.getGeo().getId());
            ps.setBytes(4, museum.getPhoto());
            ps.setObject(5, museum.getId());

            ps.executeUpdate();

            return museum;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<MuseumEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
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
                "WHERE m.id = ?"
        )) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MuseumEntity entity = MuseumEntityRowMapper.instance.mapRow(rs, 1);
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
    public List<MuseumEntity> findAll() {
        List<MuseumEntity> museums = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
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
                "JOIN country c ON g.country_id = c.id"
        )) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MuseumEntity entity = MuseumEntityRowMapper.instance.mapRow(rs, 1);
                    museums.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return museums;
    }

    @Override
    public Optional<MuseumEntity> findByTitle(String title) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
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
                "WHERE m.title = ?"
        )) {
            ps.setString(1, title);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MuseumEntity entity = MuseumEntityRowMapper.instance.mapRow(rs, 1);
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
    public void remove(MuseumEntity museum) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
            "DELETE FROM museum WHERE id = ?"
        )) {
            ps.setObject(1, museum.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
