package messagesystem.db.handlers;

import lombok.RequiredArgsConstructor;
import messagesystem.common.Serializers;
import messagesystem.db.DBService;
import messagesystem.dto.CreateUserDTO;
import messagesystem.messagesystem.Message;
import messagesystem.messagesystem.MessageType;
import messagesystem.messagesystem.RequestHandler;

import java.util.Optional;

import static messagesystem.ClientType.*;

@RequiredArgsConstructor
public class CreateUserRequestHandler implements RequestHandler {
    private final DBService dbService;

    @Override
    public Optional<Message> handle(Message msg) {
        CreateUserDTO createUserDTO = Serializers.deserialize(msg.getPayload(), CreateUserDTO.class);

        return Optional.of(
                new Message(
                        DATABASE.getType(),
                        FRONTEND.getType(),
                        msg.getId(),
                        MessageType.CREATE_USER.getValue(),
                        Serializers.serialize(dbService.createUser(createUserDTO))
                )
        );
    }
}
