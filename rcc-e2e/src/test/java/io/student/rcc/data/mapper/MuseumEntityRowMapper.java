package io.student.rcc.data.mapper;

import io.student.rcc.data.entity.api.ArtistEntity;
import io.student.rcc.data.entity.api.CountryEntity;
import io.student.rcc.data.entity.api.GeoEntity;
import io.student.rcc.data.entity.api.MuseumEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MuseumEntityRowMapper implements RowMapper<MuseumEntity> {

    public final static MuseumEntityRowMapper instance = new MuseumEntityRowMapper();

    private MuseumEntityRowMapper() {

    }

    @Override
    public MuseumEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        CountryEntity country = new CountryEntity();
        country.setId(rs.getObject("country_id", UUID.class));
        country.setName(rs.getString("country_name"));

        GeoEntity geo = new GeoEntity();
        geo.setId(rs.getObject("geo_id", UUID.class));
        geo.setCity(rs.getString("city"));
        geo.setCountry(country);


        MuseumEntity entity = new MuseumEntity();
        entity.setId(rs.getObject("museum_id", UUID.class));
        entity.setTitle(rs.getString("title"));
        entity.setDescription(rs.getString("description"));
        entity.setPhoto(rs.getBytes("photo"));
        entity.setGeo(geo);
        return entity;
    }
}
