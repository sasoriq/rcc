package io.student.rcc.services;

import io.student.rcc.model.UserJson;

public interface UsersClient {
    UserJson createUser(UserJson user);
}
