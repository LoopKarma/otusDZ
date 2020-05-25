package messagesystem.db;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import common.dto.CreateUserDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@RequiredArgsConstructor
public class DBServiceImpl implements DBService {
    private final Gson gson = new Gson();
    private final String dbServiceUserEndpoint;


    @Override
    public String requestUsers() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(dbServiceUserEndpoint)).build();

        try {
            return client.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception ex) {
            log.error("Exception ", ex);
            return "";
        }

    }

    @Override
    public String createUser(CreateUserDTO createUserDTO) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(dbServiceUserEndpoint))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(createUserDTO)))
                .header("content-type", "application/json")
                .build();

        try {
            return client.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception ex) {
            log.error("Exception ", ex);
            return null;
        }
    }
}
