package messagesystem.db.handlers;

import lombok.RequiredArgsConstructor;
import common.util.Serializers;
import messagesystem.db.DBService;
import common.dto.CreateUserDTO;
import common.messaging.Message;
import common.messaging.MessageType;
import common.messaging.RequestHandler;

import java.util.Optional;

import common.messaging.ClientType;

@RequiredArgsConstructor
public class CreateUserRequestHandler implements RequestHandler {
    private final DBService dbService;

    @Override
    public Optional<Message> handle(Message msg) {
        CreateUserDTO createUserDTO = Serializers.deserialize(msg.getPayload(), CreateUserDTO.class);

        return Optional.of(
                new Message(
                        ClientType.DATABASE.getType(),
                        ClientType.FRONTEND.getType(),
                        msg.getId(),
                        MessageType.CREATE_USER.getValue(),
                        Serializers.serialize(dbService.createUser(createUserDTO))
                )
        );
    }
}
