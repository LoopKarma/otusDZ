package messagesystem.frontend.handler;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messagesystem.messagesystem.Message;
import messagesystem.messagesystem.RequestHandler;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor
public class FrontendHandler implements RequestHandler, OutputStreamAwareHandler {
    Map<UUID, ObjectOutputStream> uuidSocketMap = new ConcurrentHashMap<>();

    @Override
    public Optional<Message> handle(Message msg) {
        getOutputStream(msg.getId())
                .or(() -> getOutputStream(msg.getSourceMessageId()))
                .ifPresent(s -> {
                    try {
                        s.writeObject(msg);
                        s.close();
                    } catch (IOException e) {
                        log.error("Creating ObjectOutputStream exception", e);
                    }
                });

        return Optional.empty();
    }

    @Override
    public void addOutputStream(ObjectOutputStream outputStream, UUID messageUuid) {
        uuidSocketMap.put(messageUuid, outputStream);
    }

    @Override
    public Optional<ObjectOutputStream> getOutputStream(UUID messageUuid) {
        if (messageUuid != null) {
            ObjectOutputStream objectOutputStream = this.uuidSocketMap.get(messageUuid);
            return Optional.ofNullable(objectOutputStream);
        }
        return Optional.empty();
    }
}
