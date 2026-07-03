package io.student.rcc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserJson(
    @JsonProperty("id") UUID id,
    @JsonProperty("username") String username,
    @JsonProperty("firstname") String firstname,
    @JsonProperty("password") String password,
    @JsonProperty("avatar") String avatar
) {}
