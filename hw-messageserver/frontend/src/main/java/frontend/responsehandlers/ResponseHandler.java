package frontend.responsehandlers;

import frontend.service.FrontendService;
import common.util.Serializers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import common.messaging.Message;
import common.messaging.RequestHandler;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
    public class ResponseHandler implements RequestHandler {
    private final FrontendService frontendService;

    @Override
    public Optional<Message> handle(Message msg) {
        log.info("New message in response handler: {}", msg);
        try {
            if (msg.getPayload().length == 0) {
                log.error("User is not present");
                return Optional.empty();
            }
            String payload = Serializers.deserialize(msg.getPayload(), String.class);
            UUID sourceMessageId = msg.getSourceMessageId();

            frontendService.takeConsumer(sourceMessageId, String.class).ifPresent(consumer -> consumer.accept(payload));
        } catch (Exception ex) {
            log.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}
