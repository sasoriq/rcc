package io.student.rcc.data.mapper;

import io.student.rcc.data.entity.api.ArtistEntity;
import io.student.rcc.data.entity.api.CountryEntity;
import io.student.rcc.data.entity.api.GeoEntity;
import io.student.rcc.data.entity.api.MuseumEntity;
import io.student.rcc.data.entity.api.PaintingEntity;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class PaintingEntityRowMapper implements RowMapper<PaintingEntity> {

    public static final PaintingEntityRowMapper instance = new PaintingEntityRowMapper();

    private PaintingEntityRowMapper() {

    }

    @Override
    public PaintingEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        ArtistEntity artist = new ArtistEntity();
        artist.setId(rs.getObject("artist_id", UUID.class));
        artist.setName(rs.getString("a_name"));
        artist.setBiography(rs.getString("a_biography"));
        artist.setPhoto(rs.getBytes("a_photo"));

        CountryEntity country = new CountryEntity();
        country.setId(rs.getObject("country_id", UUID.class));
        country.setName(rs.getString("c_name"));

        GeoEntity geo = new GeoEntity();
        geo.setId(rs.getObject("geo_id", UUID.class));
        geo.setCity(rs.getString("g_city"));
        geo.setCountry(country);

        MuseumEntity museum = new MuseumEntity();
        museum.setId(rs.getObject("museum_id", UUID.class));
        museum.setTitle(rs.getString("m_title"));
        museum.setDescription(rs.getString("m_description"));
        museum.setPhoto(rs.getBytes("m_photo"));
        museum.setGeo(geo);

        PaintingEntity painting = new PaintingEntity();
        painting.setId(rs.getObject("painting_id", UUID.class));
        painting.setTitle(rs.getString("p_title"));
        painting.setDescription(rs.getString("p_description"));
        painting.setContent(rs.getString("p_content"));
        painting.setArtist(artist);
        painting.setMuseum(museum);
        return painting;
    }
}
