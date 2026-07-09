package io.student.rcc.service;

import io.student.rcc.model.api.UserJson;

public interface UsersClient {
    UserJson createUser(UserJson user);
}
