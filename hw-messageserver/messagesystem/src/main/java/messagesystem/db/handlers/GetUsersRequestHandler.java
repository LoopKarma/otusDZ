package messagesystem.db.handlers;

import lombok.RequiredArgsConstructor;
import messagesystem.common.Serializers;
import messagesystem.db.DBService;
import messagesystem.messagesystem.Message;
import messagesystem.messagesystem.MessageType;
import messagesystem.messagesystem.RequestHandler;

import java.util.Optional;

import static messagesystem.ClientType.DATABASE;
import static messagesystem.ClientType.FRONTEND;

@RequiredArgsConstructor
public class GetUsersRequestHandler implements RequestHandler {
    private final DBService dbService;

    @Override
    public Optional<Message> handle(Message msg) {
        return Optional.of(
                new Message(
                        DATABASE.getType(),
                        FRONTEND.getType(),
                        msg.getId(),
                        MessageType.GET_ALL_USERS.getValue(),
                        Serializers.serialize(dbService.requestUsers())
                )
        );
    }
}
