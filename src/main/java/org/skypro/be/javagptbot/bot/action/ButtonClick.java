/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.bot.action;

import lombok.Data;
import org.skypro.be.javagptbot.bot.UserDialog;
import org.skypro.be.javagptbot.gigachat.GigaChatApi;
import org.skypro.be.javagptbot.utils.TextUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Data
public class ButtonClick implements BotAction {

    private final GigaChatApi gigaChatApi;
    private String questionKey;

    public ButtonClick(GigaChatApi gigaChatApi) {
        this.gigaChatApi = gigaChatApi;
    }

    @Override
    public SendMessage getAnswer(UserDialog dialog) {
        String question = dialog.getQuestionText(questionKey);
        String messageText = gigaChatApi.sendQuestion(TextUtils.getPrompt("question") + "\n" + question, dialog);
        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text(messageText)
                //TODO разобраться с ошибкой парсинга -   .parseMode("Markdown")
                .build();
    }

    @Override
    public boolean canHandle(Update update) {
        if (update.hasCallbackQuery()) {
            questionKey = update.getCallbackQuery().getData();
        }
        return update.hasCallbackQuery();
    }

    @Override
    public String getName() {
        return "ButtonClick";
    }

    @Override
    public boolean isLongOperation() {
        return true;
    }

    @Override
    public String getLongOperationMessage() {
        return "Подождите чутка... Сейчас замучу ответ!";
    }
}
