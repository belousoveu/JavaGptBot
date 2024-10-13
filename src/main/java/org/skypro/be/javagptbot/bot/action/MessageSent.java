package org.skypro.be.javagptbot.bot.action;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Data
public class MessageSent implements BotAction {

    private long chatId;

    @Override
    public SendMessage getAnswer() {
        return SendMessage.builder()
                .chatId(chatId)
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
