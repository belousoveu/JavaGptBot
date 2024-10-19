package org.skypro.be.javagptbot.gigachat;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.skypro.be.javagptbot.bot.UserDialog;
import org.skypro.be.javagptbot.gigachat.request.CompletionBody;
import org.skypro.be.javagptbot.gigachat.request.DialogMessage;
import org.skypro.be.javagptbot.gigachat.response.QueryResponse;
import org.skypro.be.javagptbot.gigachat.response.TokenResponse;
import org.skypro.be.javagptbot.utils.TextUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.skypro.be.javagptbot.gigachat.constants.GigaChatRole.SYSTEM_ROLE;
import static org.skypro.be.javagptbot.gigachat.constants.GigaChatRole.USER_ROLE;
import static org.skypro.be.javagptbot.gigachat.constants.GigaChatUrl.AUTHORIZATION_URL;
import static org.skypro.be.javagptbot.gigachat.constants.GigaChatUrl.GET_ANSWER_URL;


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

    public String sendPullRequest(String question, UserDialog dialog) {
        DialogMessage message = new DialogMessage(USER_ROLE, question);
        dialog.addMessage(message);
        List<DialogMessage> currentDialog = dialog.getMessages();
        return sendQuery(currentDialog);
    }

    public String sendQuestion(String question, UserDialog dialog) {
        DialogMessage message = new DialogMessage(USER_ROLE, question);
        List<DialogMessage> currentDialog = new ArrayList<>(dialog.getMessages());
        currentDialog.add(message);
        return sendQuery(currentDialog);
    }

    public String sendQuery(List<DialogMessage> currentDialog) {
        String bearerToken = "Bearer " + getToken();
        MediaType mediaType = MediaType.parse("application/json");
        CompletionBody body = new CompletionBody(currentDialog);
        String bodyJson = body.toJsonString();
        RequestBody bodyRequest = RequestBody.create(bodyJson, mediaType);
        Request request = new Request.Builder()
                .url(GET_ANSWER_URL)
                .method("POST", bodyRequest)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", bearerToken)
                .build();

        try {
            try (Response response = gtpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        QueryResponse queryResponse = mapper.readValue(responseBody, QueryResponse.class);
                        log.info(queryResponse.getUsage().toString());
                        return queryResponse.getChoices().get(0).getMessage().getContent();
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
        return mockText;
    }

    public List<String> getQuestions(String messageText) {
        String bearerToken = "Bearer " + getToken();
        MediaType mediaType = MediaType.parse("application/json");

        List<DialogMessage> currentDialog = new ArrayList<>();
        DialogMessage systemMessage = new DialogMessage(SYSTEM_ROLE, TextUtils.getPrompt("student"));
        DialogMessage queryMessage = new DialogMessage(USER_ROLE, TextUtils.getPrompt("questions") + "\n" + messageText);
        currentDialog.add(systemMessage);
        currentDialog.add(queryMessage);

        CompletionBody body = new CompletionBody(currentDialog);

        String bodyJson = body.toJsonString();
        RequestBody bodyRequest = RequestBody.create(bodyJson, mediaType);
        Request request = new Request.Builder()
                .url(GET_ANSWER_URL)
                .method("POST", bodyRequest)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", bearerToken)
                .build();

        try {
            try (Response response = gtpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        QueryResponse queryResponse = mapper.readValue(responseBody, QueryResponse.class);
                        String answer = queryResponse.getChoices().get(0).getMessage().getContent();
                        return TextUtils.getQuestionsList(answer);
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
        return new CopyOnWriteArrayList<>();
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
                        log.info("GigaChatApi: got new token: {}", token);
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

    private final static String mockText = "Не удалось словить ответ от GigaChat!";

}
