package messagesystem.messagesystem;

import java.util.Optional;

public interface RequestHandler {
  Optional<Message> handle(Message msg);
}
