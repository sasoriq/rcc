package io.student.rcc.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.auth.AuthorityEntity;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.params.shadow.de.siegmar.fastcsv.util.Nullable;

import java.util.UUID;

public record AuthorityJson(
    @Nullable @JsonProperty("id") UUID id,
    @NonNull @JsonProperty("authority") Authority authority,
    @NonNull @JsonProperty("user_id") AuthUserJson user
) {
    public static @NonNull AuthorityJson fromEntity(@NonNull AuthorityEntity entity) {
        return new AuthorityJson(
            entity.getId(),
            entity.getAuthority(),
            AuthUserJson.fromEntity(entity.getUser())
        );
    }
}
