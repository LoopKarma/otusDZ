package frontend.configuration;

import frontend.responsehandlers.ResponseHandler;
import frontend.service.FrontendMsClient;
import frontend.service.FrontendService;
import frontend.service.FrontendServiceImpl;
import common.messaging.MessageType;
import common.messaging.MsClient;
import common.messaging.ClientType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Value("${ms.host}")
    private String msHost;
    @Value("${ms.port}")
    private int msPort;

    @Bean
    public MsClient frontendMsgClient() {
        return new FrontendMsClient(ClientType.FRONTEND.getType(), msHost, msPort);
    }

    @Bean
    public FrontendService frontendService(MsClient frontendMsgClient) {
        FrontendService frontendService = new FrontendServiceImpl(frontendMsgClient, ClientType.DATABASE.getType());
        frontendMsgClient.addHandler(MessageType.GET_ALL_USERS, new ResponseHandler(frontendService));
        frontendMsgClient.addHandler(MessageType.CREATE_USER, new ResponseHandler(frontendService));
        return frontendService;
    }
}
