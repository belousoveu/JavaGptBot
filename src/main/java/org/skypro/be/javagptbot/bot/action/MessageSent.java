package org.skypro.be.javagptbot.bot.action;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MessageSent implements BotAction{

    private final long chatId;

    public MessageSent(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public SendMessage getAnswer() {
        return SendMessage.builder()
                .chatId(chatId)
                .text(OTHER_MESSAGE)
                .build();
    }

    private static final String OTHER_MESSAGE = """
            Извините, но тут я ничем не могу помочь.
            Перечитайте еще раз помощь - /help"
            """;
}
