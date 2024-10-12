package org.skypro.be.javagptbot.bot.action;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class ButtonClick implements BotAction{

    private final long chatId;
    private final String question;

    public ButtonClick(long chatId, String question) {
        this.question = question;
        this.chatId = chatId;
    }

    @Override
    public SendMessage getAnswer() {
        return null;
    }
}
