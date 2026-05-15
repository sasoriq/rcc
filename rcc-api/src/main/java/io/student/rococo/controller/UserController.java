package io.student.rococo.controller;

import io.student.rococo.model.UserJson;
import io.student.rococo.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserJson getUser(@AuthenticationPrincipal Jwt principal) {
        return userService.getUser(principal);
    }

    @PatchMapping
    public UserJson updateUser(@RequestBody UserJson updateRequest, @AuthenticationPrincipal Jwt principal) {
        return userService.updateUser(updateRequest, principal);
    }
}
