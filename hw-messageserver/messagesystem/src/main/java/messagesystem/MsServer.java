package messagesystem;

import common.messaging.ClientType;
import common.messaging.MessageType;
import common.messaging.MsClient;
import lombok.extern.slf4j.Slf4j;
import messagesystem.db.DBService;
import messagesystem.db.DBServiceImpl;
import messagesystem.db.handlers.CreateUserRequestHandler;
import messagesystem.db.handlers.GetUsersRequestHandler;
import messagesystem.frontend.handler.FrontendHandler;
import messagesystem.messagesystem.*;

import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class MsServer {
    public static final String DB_SERVICE_USER_ENDPOINT = "http://localhost:8090/user";
    private static final int SOCKET_PORT = 8080;

    public static void main(String[] args) {
        new MsServer().start();
    }

    private void start() {
        //initializing MS application dependencies
        MessageSystem messageSystem = new MessageSystemImpl();
        MsClient msClientFrontend = new MsClientImpl(ClientType.FRONTEND.getType(), messageSystem);
        MsClient msClientDatabase = new MsClientImpl(ClientType.DATABASE.getType(), messageSystem);

        DBService dbService = new DBServiceImpl(DB_SERVICE_USER_ENDPOINT);
        CreateUserRequestHandler createUserRequestHandler = new CreateUserRequestHandler(dbService);
        GetUsersRequestHandler getUsersRequestHandler = new GetUsersRequestHandler(dbService);

        msClientDatabase.addHandler(MessageType.CREATE_USER, createUserRequestHandler);
        msClientDatabase.addHandler(MessageType.GET_ALL_USERS, getUsersRequestHandler);

        FrontendHandler frontendHandler = new FrontendHandler();
        msClientFrontend.addHandler(MessageType.CREATE_USER, frontendHandler);
        msClientFrontend.addHandler(MessageType.GET_ALL_USERS, frontendHandler);

        messageSystem.addClient(msClientDatabase);
        messageSystem.addClient(msClientFrontend);

        serveAsSocketServer(messageSystem, frontendHandler);
    }

    private void serveAsSocketServer(MessageSystem messageSystem, FrontendHandler frontendHandler) {
        try (ServerSocket serverSocket = new ServerSocket(SOCKET_PORT)) {
            while (!Thread.currentThread().isInterrupted()) {
                log.info("waiting for client connection");
                Socket clientSocket = serverSocket.accept();
                Runnable runnable = new SocketProcessor(clientSocket, messageSystem, frontendHandler);
                Thread thread = new Thread(runnable);
                thread.start();
            }
        } catch (Exception ex) {
            log.error("error", ex);
        }
    }
}
