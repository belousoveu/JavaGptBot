/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.bot.action;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.skypro.be.javagptbot.bot.UserDialog;
import org.skypro.be.javagptbot.github.exception.GithubAuthenticationException;
import org.skypro.be.javagptbot.github.exception.InvalidPullRequestLinkException;
import org.skypro.be.javagptbot.gigachat.request.DialogMessage;
import org.skypro.be.javagptbot.gigachat.GigaChatApi;
import org.skypro.be.javagptbot.github.GithubService;
import org.skypro.be.javagptbot.utils.TextUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.skypro.be.javagptbot.gigachat.constants.GigaChatRole.ASSISTANT_ROLE;

@Slf4j
@Component
@Data
public class LinkSent implements BotAction {

    private String link;
    private final GithubService gitHubService;
    private final GigaChatApi gigaChatApi;

    public LinkSent(GithubService gitHubService, GigaChatApi gigaChatApi) {
        this.gitHubService = gitHubService;
        this.gigaChatApi = gigaChatApi;
    }

    @Override
    public SendMessage getAnswer(UserDialog dialog) {
        Map<String, String> projectFiles;
        String messageText;
        List<String> questionList = new ArrayList<>();
        dialog.clearMessages();
        dialog.setQuestions(new HashMap<>());
        try {
            projectFiles = gitHubService.getProjectFiles(link);
            String queryText = TextUtils.convertToString(projectFiles);
            messageText = gigaChatApi.sendPullRequest(TextUtils.getPrompt("task") + "\n" + queryText, dialog);
            dialog.addMessage(new DialogMessage(ASSISTANT_ROLE, messageText));

            if (gigaChatApi.mockText.equals(messageText)) {
                questionList = new ArrayList<>();
            } else {
                questionList = gigaChatApi.getQuestions(messageText);
            }

        } catch (InvalidPullRequestLinkException | GithubAuthenticationException e) {
            messageText = e.getMessage();
        } catch (IOException e) {
            log.error(e.getMessage());
            messageText = "Unknown error occurred. Please try again later";
        }
        InlineKeyboardMarkup buttons = convertToInlineKeyboardMarkup(questionList, dialog);

        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text(messageText)
                //TODO разобраться с ошибкой парсинга -   .parseMode("Markdown")
                .replyMarkup(buttons)
                .build();
    }


    @Override
    public boolean canHandle(Update update) {
        if (update.hasMessage()
                && update.getMessage().getText().startsWith("https://github.com/")
                && update.getMessage().getText().contains("pull")) {
            link = update.getMessage().getText();
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "LinkSent";
    }

    @Override
    public boolean isLongOperation() {
        return true;
    }

    @Override
    public String getLongOperationMessage() {
        return "Погоди немного... Я сейчас заценю проект и соберу ответ!";
    }

    private InlineKeyboardMarkup convertToInlineKeyboardMarkup(List<String> questionList, UserDialog dialog) {
        List<InlineKeyboardRow> result = new ArrayList<>();
        Map<String, String> questionMap = new HashMap<>();

        int i = 1;
        for (String question : questionList) {
            String buttonKey = "question" + i + "_" + dialog.getChatId();
            result.add(new InlineKeyboardRow(InlineKeyboardButton.builder().text(question).callbackData(buttonKey).build()));
            questionMap.put(buttonKey, question);
            i++;
        }
        dialog.setQuestions(questionMap);
        return new InlineKeyboardMarkup(result);
    }
}
