package io.student.rcc.services;

import io.student.rcc.model.api.UserJson;

public interface UsersClient {
    UserJson createUser(UserJson user);
}
