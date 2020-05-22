package frontend.service;

import frontend.util.Serializers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messagesystem.messagesystem.Message;
import messagesystem.messagesystem.MessageType;
import messagesystem.messagesystem.MsClient;
import messagesystem.messagesystem.RequestHandler;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class FrontendMsClient implements MsClient {
    private final String name;
    private final String msHost;
    private final int msPort;
    private final Map<String, RequestHandler> handlers = new ConcurrentHashMap<>();

    @Override
    public void addHandler(MessageType type, RequestHandler requestHandler) {
        this.handlers.put(type.getValue(), requestHandler);
    }

    @Override
    public boolean sendMessage(Message msg) {
        try {
            try (Socket clientSocket = new Socket(msHost, msPort)) {
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());

                log.info("sending message type {} to MS", msg.getType());
                outputStream.writeObject(msg);

                Message responseMessage = (Message) inStream.readObject();
                log.info("server response: {}", responseMessage.getType());

                log.info("stop communication");
                outputStream.close();
                inStream.close();

                this.handlers.get(responseMessage.getType()).handle(responseMessage);

                return true;
            }
        } catch (Exception ex) {
            log.error("error", ex);
        }
        return false;
    }

    @Override
    public void handle(Message msg) {
        log.info("new message:{}", msg);
        try {
            RequestHandler requestHandler = handlers.get(msg.getType());
            if (requestHandler != null) {
                requestHandler.handle(msg).ifPresent(this::sendMessage);
            } else {
                log.error("handler not found for the message type:{}", msg.getType());
            }
        } catch (Exception ex) {
            log.error("msg:" + msg, ex);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public <T> Message produceMessage(String to, T data, MessageType msgType) {
        return new Message(name, to, null, msgType.getValue(), Serializers.serialize(data));
    }
}
