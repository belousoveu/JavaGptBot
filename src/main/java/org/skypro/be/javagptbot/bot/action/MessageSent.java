package org.skypro.be.javagptbot.bot.action;

import lombok.Data;
import org.skypro.be.javagptbot.bot.UserDialog;
import org.skypro.be.javagptbot.gigachat.GigaChatApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageSent implements BotAction {

    @Autowired
    private GigaChatApi gigaChatApi; //TODO позже удалить


    @Override
    public SendMessage getAnswer(UserDialog dialog) {
        System.out.println("gigaChatApi.getToken() = " + gigaChatApi.getToken()); //TODO позже удалить
        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text(OTHER_MESSAGE)
                .build();
    }

    @Override
    public boolean canHandle(Update update) {
        return true;
    }

    @Override
    public String getName() {
        return "MessageSent";
    }

    private static final String OTHER_MESSAGE = """
            Извините, но тут я ничем не могу помочь.
            Перечитайте еще раз помощь - /help"
            """;
}
