package io.student.rcc.data.mapper;

import io.student.rcc.data.entity.api.ArtistEntity;
import io.student.rcc.data.entity.api.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ArtistEntityRowMapper implements RowMapper<ArtistEntity> {

    public final static ArtistEntityRowMapper instance = new ArtistEntityRowMapper();

    private ArtistEntityRowMapper() {

    }

    @Override
    public ArtistEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        ArtistEntity entity = new ArtistEntity();
        entity.setId(rs.getObject("id", UUID.class));
        entity.setName(rs.getString("name"));
        entity.setBiography(rs.getString("biography"));
        entity.setPhoto(rs.getBytes("photo"));
        return entity;
    }
}
