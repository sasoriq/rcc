package io.student.rococo.model;

import io.student.rococo.data.entity.UserEntity;

import java.util.UUID;

public record UserJson(UUID id, String username, String firstname, String lastname, String avatar) {
    public static UserJson fromEntity(UserEntity entity) {
        return new UserJson(
            entity.getId(),
            entity.getUsername(),
            entity.getFirstname(),
            entity.getLastname(),
            entity.getAvatar()
        );
    }
}
