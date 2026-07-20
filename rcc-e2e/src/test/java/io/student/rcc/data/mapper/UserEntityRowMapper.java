package io.student.rcc.data.mapper;

import io.student.rcc.data.entity.api.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserEntityRowMapper implements RowMapper<UserEntity> {

    public static final UserEntityRowMapper instance = new UserEntityRowMapper();

    private UserEntityRowMapper() {

    }

    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEntity entity = new UserEntity();
        entity.setId(rs.getObject("id", UUID.class));
        entity.setUsername(rs.getString("username"));
        entity.setFirstname(rs.getString("firstname"));
        entity.setLastname(rs.getString("lastname"));
        entity.setAvatar(rs.getBytes("avatar"));
        return entity;
    }
}
