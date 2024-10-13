package org.skypro.be.javagptbot.bot;

import org.skypro.be.javagptbot.bot.action.BotAction;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class BotActionFactory {

    private final List<BotAction> actions;

    public BotActionFactory(List<BotAction> actions) {
        this.actions = actions;
    }

    public BotAction getAction(Update update) {
        if (update == null) {
            throw new NullPointerException("Update is null"); //TODO: класс ошибки
        }
        long chatId = getChatId(update);
        BotAction defaultAction = actions.stream().filter(a -> "MessageSent".equals(a.getName())).findFirst().orElseThrow();
        for (BotAction action : actions) {
            if (!action.getName().equals(defaultAction.getName()) && action.canHandle(update)) {
                action.setChatId(chatId);
                return action;
            }
        }

        defaultAction.setChatId(chatId);
        return defaultAction;

    }


    private static long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }
        throw new IllegalArgumentException("No chat id"); //TODO: класс ошибки
    }
}
