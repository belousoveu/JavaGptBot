package org.skypro.be.javagptbot.bot.action;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Data
public class HelpCommand implements BotAction {

    private long chatId;

    @Override
    public SendMessage getAnswer() {
        return SendMessage.builder()
                .chatId(chatId)
                .text(HELP_MESSAGE)
                .build();
    }

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().getText().startsWith("/help");
    }

    @Override
    public String getName() {
        return "HelpCommand";
    }

    private static final String HELP_MESSAGE = """
             Для того чтобы воспользоваться моей помощью пришлите
             ссылку на пулл-реквест в репозитории проекта.
             сообщение с ссылкой должно выглядеть примерно так:
             '''https://github.com/{username}/{project-name}/pull/1'''
            """;
}
