package messagesystem.frontend.handler;

import java.io.ObjectOutputStream;
import java.util.Optional;
import java.util.UUID;

public interface OutputStreamAwareHandler {
    void addOutputStream(ObjectOutputStream outputStream, UUID messageUuid);
    Optional<ObjectOutputStream> getOutputStream(UUID messageUuid);
}
