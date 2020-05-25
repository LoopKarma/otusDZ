package frontend.service;

import common.dto.CreateUserDTO;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendService {
  void createUser(CreateUserDTO createUserDTO, Consumer<String> dataConsumer);
  void getAllUsers(Consumer<String> dataConsumer);

  <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);
}

