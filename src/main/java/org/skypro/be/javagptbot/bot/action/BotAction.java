package org.skypro.be.javagptbot.bot.action;

import org.skypro.be.javagptbot.bot.UserDialog;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotAction {
    SendMessage getAnswer(UserDialog dialog);

    boolean canHandle(Update update);

    String getName();

    boolean isLongOperation();

    String getLongOperationMessage();
}
