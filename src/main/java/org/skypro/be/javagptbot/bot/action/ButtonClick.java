package org.skypro.be.javagptbot.bot.action;

import lombok.Data;
import org.skypro.be.javagptbot.bot.UserDialog;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Data
public class ButtonClick implements BotAction {

    private String questionKey;

    @Override
    public SendMessage getAnswer(UserDialog dialog) {
        String question = dialog.getQuestionText(questionKey);
        System.out.println("question = " + question); //TODO remove
        return SendMessage.builder()
                .chatId(dialog.getChatId())
                .text("Нажатие кнопки")
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
}
