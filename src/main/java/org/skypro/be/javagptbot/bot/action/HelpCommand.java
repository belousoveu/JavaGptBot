package org.skypro.be.javagptbot.bot.action;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class HelpCommand implements BotAction {

    private final long chatId;

    public HelpCommand(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public SendMessage getAnswer() {
        return SendMessage.builder()
                .chatId(chatId)
                .text(HELP_MESSAGE)
                .build();
    }

    private static final String HELP_MESSAGE = """
            Для того чтобы воспользоваться моей помощью пришлите
            ссылку на пулл-реквест в репозитории проекта.
            сообщение с ссылкой должно выглядеть примерно так:
            https://github.com/{username}/{project-name}}/pull/1
           """;
}
