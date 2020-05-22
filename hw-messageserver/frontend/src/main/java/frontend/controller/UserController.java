package frontend.controller;

import frontend.service.FrontendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messagesystem.dto.CreateUserDTO;
import frontend.dto.WebSocketMessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    public static final String TOPIC_USERS = "/topic/users";

    private final FrontendService frontendService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/user")
    public ResponseEntity createUser(CreateUserDTO createUserDTO) {
        log.info("Request for creating createUserDTO: {}", createUserDTO);
        frontendService.createUser(createUserDTO, user -> {
            simpMessagingTemplate.convertAndSend(TOPIC_USERS, new WebSocketMessageDTO(user));
        });

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/user")
    public ResponseEntity getAllUsers() {
        log.info("Request all users");
        frontendService.getAllUsers(list -> {
            simpMessagingTemplate.convertAndSend(TOPIC_USERS, new WebSocketMessageDTO(list));
        });

        return ResponseEntity.accepted().build();
    }
}
