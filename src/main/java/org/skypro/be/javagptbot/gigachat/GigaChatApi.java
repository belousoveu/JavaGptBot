package org.skypro.be.javagptbot.gigachat;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.skypro.be.javagptbot.bot.UserDialog;
import org.skypro.be.javagptbot.gigachat.response.GigaChatResponse;
import org.skypro.be.javagptbot.gigachat.response.TokenResponse;
import org.skypro.be.javagptbot.utils.TextUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.skypro.be.javagptbot.gigachat.constants.GigaChatRole.*;
import static org.skypro.be.javagptbot.gigachat.constants.GigaChatUrl.*;


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

        // TODO Не удалять. Код рабочий. Закомментил для тестов.
        try {
            try (Response response = gtpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        GigaChatResponse gigaChatResponse = mapper.readValue(responseBody, GigaChatResponse.class);
                        String answer = gigaChatResponse.getChoices().get(0).getMessage().getContent();
                        System.out.println("gigaChatResponse.getUsage().toString() = " + gigaChatResponse.getUsage().toString()); //TODO remove
                        return answer;
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
                        GigaChatResponse gigaChatResponse = mapper.readValue(responseBody, GigaChatResponse.class);
                        String answer = gigaChatResponse.getChoices().get(0).getMessage().getContent();
                        // TODO
                        System.out.println("answer = " + answer); //TODO remove
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

    private final static String mockText = """
            В представленном проекте нет явных ошибок в структуре проекта, соответствия принципам ООП и "чистого кода". Однако есть некоторые моменты, которые можно улучшить для лучшей поддержки и сопровождения кода:
            
            1. Принципы SOLID:
               - Single Responsibility Principle (SRP): Классы `Truck`, `Car` и `Bicycle` имеют слишком много методов, что делает их сложными для понимания и поддержки. Каждый класс должен иметь одну основную ответственность.
               - Liskov Substitution Principle (LSP): Метод `check()` класса `Vehicle` не реализует интерфейс `ServiceStation`. Это может привести к проблемам при использовании этого метода в других частях системы.
               - Open/Closed Principle (OCP): Класс `Vehicle` имеет методы `check()`, `checkEngine()` и `checkTrailer()`. Эти методы должны быть открыты для расширения, но закрыты для модификации.
            
            2. Принципы KISS:
               - Keep It Simple Stupid (KISS): В классе `Vehicle` есть метод `updateTyre()`, который дублирует логику из `check()`. Это усложняет понимание кода и увеличивает вероятность ошибок.
            
            3. Принципы YAGNI:
               - You Ain't Gonna Need It (YAGNI): В классе `Bicycle` отсутствует конструктор с параметрами, хотя он наследуется от `Vehicle`, который имеет такой конструктор. Это может вызвать проблемы при добавлении новых свойств в будущем.
            
            4. DRY:
               - Don't Repeat Yourself (DRY): В классе `Vehicle` есть повторяющаяся логика проверки двигателя и прицепа в методах `check()` и `checkEngine()`. Это нарушает принцип DRY.
            
            5. Параметры конструктора:
               - В классе `Vehicle` используется один конструктор без параметров, что может затруднить создание экземпляров классов, наследующихся от него.
            
            6. Тестирование:
               - Тестирование отсутствует, что является недостатком для проекта, так как тестирование помогает обнаруживать ошибки и повышает качество кода.
            
            Рекомендации:
            - Разделите логику проверки между различными методами в классе `Vehicle`.
            - Добавьте конструктор с параметрами в класс `Bicycle`.
            - Используйте фабричные методы для создания экземпляров классов, наследующихся от `Vehicle`.
            - Добавьте тесты для каждого класса, чтобы обеспечить его корректность.
            - Убедитесь, что каждый класс соответствует принципу единственной ответственности.
            
            """;
}
