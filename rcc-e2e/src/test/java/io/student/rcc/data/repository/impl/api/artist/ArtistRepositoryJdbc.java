package io.student.rcc.data.repository.impl.api.artist;

import io.student.rcc.config.Config;
import io.student.rcc.data.entity.api.ArtistEntity;
import io.student.rcc.data.mapper.ArtistEntityRowMapper;
import io.student.rcc.data.repository.ArtistRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.student.rcc.data.tpl.Connections.holder;

public class ArtistRepositoryJdbc implements ArtistRepository {

    private static final Config CFG = Config.getInstance();
    @Override
    public ArtistEntity create(ArtistEntity artist) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
            "INSERT INTO artist (name, biography, photo) " +
                "VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, artist.getName());
            ps.setString(2, artist.getBiography());
            ps.setBytes(3, artist.getPhoto());
            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            artist.setId(generatedKey);
            return artist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArtistEntity update(ArtistEntity artist) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
            "UPDATE artist SET name = ?, biography = ?, photo = ? " +
                "WHERE id = ?"
        )) {
            ps.setString(1, artist.getName());
            ps.setString(2, artist.getBiography());
            ps.setBytes(3, artist.getPhoto());
            ps.setObject(4, artist.getId());

            ps.executeUpdate();

            return artist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ArtistEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
            "SELECT * FROM artist WHERE id = ?"
        )) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ArtistEntity entity = ArtistEntityRowMapper.instance.mapRow(rs, 1);
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
    public List<ArtistEntity> findAll() {
        List<ArtistEntity> artists = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
            "SELECT * FROM artist"
        )) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ArtistEntity entity = ArtistEntityRowMapper.instance.mapRow(rs, 1);
                    artists.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return artists;
    }

    @Override
    public Optional<ArtistEntity> findByName(String name) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
            "SELECT * FROM artist WHERE name = ?"
        )) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ArtistEntity entity = ArtistEntityRowMapper.instance.mapRow(rs, 1);
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
    public void remove(ArtistEntity artist) {
        try (PreparedStatement ps = holder(CFG.apiJdbcUrl()).connection().prepareStatement(
            "DELETE FROM artist WHERE id = ?"
        )) {
            ps.setObject(1, artist.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
