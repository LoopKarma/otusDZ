package ru.otus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.CreateUserDTO;
import ru.otus.dto.Message;
import ru.otus.front.FrontendService;
import ru.otus.model.User;
import ru.otus.repository.UserRepository;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final FrontendService frontendService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @PostMapping("/user")
    public ResponseEntity createUser(CreateUserDTO createUserDTO) throws JsonProcessingException {
        log.info("Request for creating createUserDTO: {}", createUserDTO);
        frontendService.createUser(createUserDTO, user -> {
            try {
                simpMessagingTemplate.convertAndSend("/topic/users/response", new Message(objectMapper.writeValueAsString(user)));
            } catch (JsonProcessingException e) {
                log.error("Unable to create json from user", e);
            }
        });

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/user")
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }
}
