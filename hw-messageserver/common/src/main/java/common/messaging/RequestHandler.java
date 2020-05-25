package common.messaging;

import java.util.Optional;

public interface RequestHandler {
  Optional<Message> handle(Message msg);
}
