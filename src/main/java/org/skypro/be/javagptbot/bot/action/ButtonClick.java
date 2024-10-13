package org.skypro.be.javagptbot.bot.action;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Data
public class ButtonClick implements BotAction {

    private long chatId;
    private String question;

    @Override
    public SendMessage getAnswer() {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Нажатие кнопки")
                .build();
    }

    @Override
    public boolean canHandle(Update update) { //TODO: сделать обработку нажатия кнопки
        return update.hasCallbackQuery();
    }

    @Override
    public String getName() {
        return "ButtonClick";
    }
}
