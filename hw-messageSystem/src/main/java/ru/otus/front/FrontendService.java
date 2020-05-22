package ru.otus.front;


import ru.otus.dto.CreateUserDTO;
import ru.otus.model.User;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendService {
  void createUser(CreateUserDTO createUserDTO, Consumer<User> dataConsumer);

  <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);
}

