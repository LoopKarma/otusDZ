package messagesystem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messagesystem.frontend.handler.FrontendHandler;
import messagesystem.messagesystem.Message;
import messagesystem.messagesystem.MessageSystem;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
@RequiredArgsConstructor
class SocketProcessor implements Runnable {
    private final Socket socket;
    private final MessageSystem messageSystem;
    private final FrontendHandler frontendHandler;

    @Override
    public void run() {
        try  {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

            Message message = (Message) inStream.readObject();
            log.info("Got new message with type: {}", message.getType());
            messageSystem.newMessage(message);
            frontendHandler.addOutputStream(outputStream, message.getId());
        } catch (Exception ex) {
            log.error("error", ex);
        }
    }
}
