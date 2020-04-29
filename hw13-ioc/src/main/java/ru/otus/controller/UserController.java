package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.model.User;
import ru.otus.repository.UserRepository;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping({"/", "/user/list"})
    public String userListView(Model model) {
        List<User> users = userRepository.findAll();
        log.info("All users {}", users);
        model.addAttribute("users", users);

        return "userList";
    }

    @GetMapping("/user/create")
    public String userCreateView(Model model) {
        model.addAttribute("user", new User());
        return "userCreate";
    }

    @PostMapping("/user/create")
    public RedirectView userCreate(@ModelAttribute User user) {
        log.info("User will be created {}", user);
        userRepository.save(user);
        return new RedirectView("/user/list", true);
    }
}
