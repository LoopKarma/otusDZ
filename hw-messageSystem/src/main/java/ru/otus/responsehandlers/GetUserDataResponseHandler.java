package ru.otus.responsehandlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.app.common.Serializers;
import ru.otus.front.FrontendService;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.model.User;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class GetUserDataResponseHandler implements RequestHandler {

  private final FrontendService frontendService;

  @Override
  public Optional<Message> handle(Message msg) {
    log.info("New message in response handler: {}", msg);
    try {
      if (msg.getPayload().length == 0) {
        log.error("User is not present");
        return Optional.empty();
      }
      User user = Serializers.deserialize(msg.getPayload(), User.class);
      UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
      frontendService.takeConsumer(sourceMessageId, User.class).ifPresent(consumer -> consumer.accept(user));

    } catch (Exception ex) {
      log.error("msg:" + msg, ex);
    }
    return Optional.empty();
  }
}
