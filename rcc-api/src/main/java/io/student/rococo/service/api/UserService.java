package io.student.rococo.service.api;

import io.student.rococo.data.entity.UserEntity;
import io.student.rococo.data.repository.UserRepository;
import io.student.rococo.model.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static io.student.rococo.model.UserJson.fromEntity;

@Component
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserJson getUser(Jwt principal) {
        String username = principal.getSubject();

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() ->
               new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        return fromEntity(userEntity);
    }

    public UserJson updateUser(UserJson updateRequest, Jwt principal) {
        String username = principal.getSubject();

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        userEntity.setFirstname(updateRequest.firstname());
        userEntity.setLastname(updateRequest.lastname());
        UserEntity updatedUser = userRepository.save(userEntity);

        return fromEntity(updatedUser);
    }
}
