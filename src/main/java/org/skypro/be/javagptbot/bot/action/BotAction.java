package org.skypro.be.javagptbot.bot.action;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotAction {
    SendMessage getAnswer();
}
