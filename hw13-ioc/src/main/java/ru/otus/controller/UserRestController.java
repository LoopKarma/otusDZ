package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.model.User;
import ru.otus.repository.UserRepository;

import java.util.Collection;

@RequestMapping("v1/users")
@RestController
@RequiredArgsConstructor
public class UserRestController {
    private final UserRepository userRepository;

    @GetMapping
    public Collection<User> getUsers() {
        return userRepository.findAll();
    }
}
