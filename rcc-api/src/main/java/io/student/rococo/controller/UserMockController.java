package io.student.rococo.controller;

import io.student.rococo.model.UserJson;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserMockController extends MockController<UserJson> {

    @Override
    protected String getMockResourcePath() {
        return "mock/user";
    }

    @Override
    protected Class<UserJson> getItemClass() {
        return UserJson.class;
    }

    @Override
    protected String getItemId(UserJson item) {
        return item.id().toString();
    }

    @Override
    protected String getResourceNotFoundMessage(String id) {
        return "User with id \"" + id + "\" not found";
    }

    @GetMapping
    public UserJson getUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaimAsString("sub");
        return getFilteredData(user -> user.username()
                .equalsIgnoreCase(username));
    }

    @PatchMapping
    public UserJson updateUser(@AuthenticationPrincipal Jwt principal, @RequestBody UserJson user) {
        return user;
    }
}
