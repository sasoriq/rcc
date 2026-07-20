package io.student.rcc.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.auth.AuthUserEntity;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public record AuthUserJson(
    @NonNull @JsonProperty("id") UUID id,
    @NonNull @JsonProperty("username") String username,
    @NonNull @JsonProperty("password") String password,
    @NonNull @JsonProperty("enabled") Boolean enabled,
    @NonNull @JsonProperty("account_non_expired") Boolean accountNonExpired,
    @NonNull @JsonProperty("account_non_locked") Boolean accountNonLocked,
    @NonNull @JsonProperty("credentials_non_expired") Boolean credentialsNonExpired
) {
    public static @NonNull AuthUserJson fromEntity(@NonNull AuthUserEntity entity) {
        return new AuthUserJson(
            entity.getId(),
            entity.getUsername(),
            entity.getPassword(),
            entity.getEnabled(),
            entity.getAccountNonExpired(),
            entity.getAccountNonLocked(),
            entity.getCredentialsNonExpired()
        );
    }
}
