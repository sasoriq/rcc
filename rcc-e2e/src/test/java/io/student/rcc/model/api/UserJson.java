package io.student.rcc.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.api.UserEntity;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Base64;
import java.util.UUID;

@ParametersAreNonnullByDefault
public record UserJson(
    @JsonProperty("id") UUID id,
    @JsonProperty("username") String username,
    @Nullable @JsonProperty("firstname") String firstname,
    @Nullable @JsonProperty("lastname") String lastname,
    @Nullable @JsonProperty("avatar") String avatar
) {
    public static UserJson fromEntity(UserEntity entity) {
        return new UserJson(
            entity.getId(),
            entity.getUsername(),
            entity.getFirstname(),
            entity.getLastname(),
            entity.getAvatar() != null
                ? Base64.getEncoder().encodeToString(entity.getAvatar())
                : null
        );
    }
}
