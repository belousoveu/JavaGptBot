package org.skypro.be.javagptbot.gigachat;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.skypro.be.javagptbot.gigachat.response.TokenResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.skypro.be.javagptbot.gigachat.GigaChatUrl.AUTHORIZATION_URL;


@Component
@Data
@Slf4j
public class GigaChatApi {

    private final OkHttpClient gtpClient;
    private final String gigaChatAuthorizationKey;
    private final String gigaChatKeyScope;
    private String token;
    private long tokenExpiration;

    public GigaChatApi(@Qualifier("gigaChatClient") OkHttpClient gtpClient,
                       @Qualifier("gigaChatAuthorizationKey") String gigaChatAuthorizationKey,
                       @Qualifier("gigaChatKeyScope") String gigaChatKeyScope) {
        this.gtpClient = gtpClient;
        this.gigaChatAuthorizationKey = gigaChatAuthorizationKey;
        this.gigaChatKeyScope = gigaChatKeyScope;
    }

    public String getToken() {
        if (token != null && tokenExpiration > System.currentTimeMillis() / 1000) {
            return token;
        }
        setToken();
        return token;
    }

    public String sendQuery(String question) {

        return "Это полученный ответ от GigaChat";
    }

    public List<String> getQuestions(String messageText) {

        return new CopyOnWriteArrayList<>(List.of("Это сформированный вопрос №1", "Это сформированный вопрос №2", "Это сформированный вопрос №3"));
    }

    private void setToken() {
        String rqUID = UUID.randomUUID().toString();
        String key = "Basic " + gigaChatAuthorizationKey;

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(gigaChatKeyScope, mediaType);
        Request request = new Request.Builder()
                .url(AUTHORIZATION_URL)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .addHeader("RqUID", rqUID)
                .addHeader("Authorization", key)
                .build();
        try {
            try (Response response = gtpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        TokenResponse tokenResponse = mapper.readValue(responseBody, TokenResponse.class);
                        token = tokenResponse.getAccessToken();
                        tokenExpiration = tokenResponse.getExpiresAt();
                    } catch (JacksonException e) {
                        log.error(e.getMessage());
                    }
                } else {
                    log.error("GigaChatApi: {} {}", response.code(), response.message());
                }
            }
        } catch (IOException | NullPointerException e) {
            log.error(e.getMessage());
        }
    }
}
