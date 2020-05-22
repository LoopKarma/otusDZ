package ru.otus.front;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.dto.CreateUserDTO;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import ru.otus.model.User;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class FrontendServiceImpl implements FrontendService {

  private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
  private final MsClient msClient;
  private final String databaseServiceClientName;

  @Override
  public void createUser(CreateUserDTO createUserDTO, Consumer<User> dataConsumer) {
    Message outMsg = msClient.produceMessage(databaseServiceClientName, createUserDTO, MessageType.CREATE_USER);
    consumerMap.put(outMsg.getId(), dataConsumer);
    msClient.sendMessage(outMsg);
  }

  @Override
  public <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass) {
    Consumer<T> consumer = (Consumer<T>) consumerMap.remove(sourceMessageId);
    if (consumer == null) {
      log.warn("consumer not found for:{}", sourceMessageId);
      return Optional.empty();
    }
    return Optional.of(consumer);
  }
}
