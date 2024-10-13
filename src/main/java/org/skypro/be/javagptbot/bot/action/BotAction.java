package org.skypro.be.javagptbot.bot.action;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotAction {
    SendMessage getAnswer();

    boolean canHandle(Update update);

    void setChatId(long chatId);

    String getName();
}
