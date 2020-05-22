package ru.otus.requesthandlers;

import lombok.RequiredArgsConstructor;
import ru.otus.app.common.Serializers;
import ru.otus.dto.CreateUserDTO;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.model.User;
import ru.otus.repository.UserRepository;
import java.util.Optional;


@RequiredArgsConstructor
public class CreateUserRequestHandler implements RequestHandler {
    private final UserRepository userRepository;

    @Override
    public Optional<Message> handle(Message msg) {
        CreateUserDTO createUserDTO = Serializers.deserialize(msg.getPayload(), CreateUserDTO.class);
        User user = User.builder()
                .login(createUserDTO.getLogin())
                .name(createUserDTO.getName())
                .password(createUserDTO.getPassword())
                .build();
        userRepository.saveAndFlush(user);

        byte[] payload = Serializers.serialize(user);
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), Optional.of(msg.getId()), MessageType.CREATE_USER.getValue(), payload));
    }
}
