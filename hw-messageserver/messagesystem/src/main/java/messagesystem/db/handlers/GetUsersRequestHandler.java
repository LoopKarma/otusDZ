package messagesystem.db.handlers;

import lombok.RequiredArgsConstructor;
import common.util.Serializers;
import messagesystem.db.DBService;
import common.messaging.Message;
import common.messaging.MessageType;
import common.messaging.ClientType;
import common.messaging.RequestHandler;

import java.util.Optional;

@RequiredArgsConstructor
public class GetUsersRequestHandler implements RequestHandler {
    private final DBService dbService;

    @Override
    public Optional<Message> handle(Message msg) {
        return Optional.of(
                new Message(
                        ClientType.DATABASE.getType(),
                        ClientType.FRONTEND.getType(),
                        msg.getId(),
                        MessageType.GET_ALL_USERS.getValue(),
                        Serializers.serialize(dbService.requestUsers())
                )
        );
    }
}
