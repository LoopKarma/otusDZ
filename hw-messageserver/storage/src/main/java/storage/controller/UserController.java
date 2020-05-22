package storage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messagesystem.dto.CreateUserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import storage.model.User;
import storage.repository.UserRepository;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/user")
    public User createUser(@RequestBody CreateUserDTO createUserDTO) {
        log.info("Request for creating createUserDTO: {}", createUserDTO);
        User user = User.builder()
                .login(createUserDTO.getLogin())
                .name(createUserDTO.getName())
                .password(createUserDTO.getPassword())
                .build();

        userRepository.saveAndFlush(user);
        return user;
    }

    @GetMapping("/user")
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }
}
