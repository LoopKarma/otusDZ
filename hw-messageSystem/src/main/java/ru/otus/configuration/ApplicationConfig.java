package ru.otus.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.requesthandlers.CreateUserRequestHandler;
import ru.otus.front.FrontendService;
import ru.otus.front.FrontendServiceImpl;
import ru.otus.responsehandlers.GetUserDataResponseHandler;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.MsClientImpl;
import ru.otus.repository.UserRepository;

@Configuration
public class ApplicationConfig {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Bean
    public MsClient databaseMsgClient(MessageSystem messageSystem, UserRepository userRepository) {
        MsClientImpl msClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem);
        msClient.addHandler(MessageType.CREATE_USER, new CreateUserRequestHandler(userRepository));

        messageSystem.addClient(msClient);
        return msClient;
    }

    @Bean
    public MsClient frontendMsgClient(MessageSystem messageSystem) {
        return new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem);
    }

    @Bean
    public FrontendService frontendService(MessageSystem messageSystem, MsClient frontendMsgClient) {
        FrontendService frontendService = new FrontendServiceImpl(frontendMsgClient, DATABASE_SERVICE_CLIENT_NAME);
        frontendMsgClient.addHandler(MessageType.CREATE_USER, new GetUserDataResponseHandler(frontendService));
        messageSystem.addClient(frontendMsgClient);
        return frontendService;
    }
}
