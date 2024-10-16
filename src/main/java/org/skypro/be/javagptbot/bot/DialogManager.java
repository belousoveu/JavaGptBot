package org.skypro.be.javagptbot.bot;

import lombok.Getter;
import lombok.Setter;
import org.skypro.be.javagptbot.bot.action.BotAction;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DialogManager {

    private final Map<Long,UserDialog> dialogs= new ConcurrentHashMap<>();
    private final List<BotAction> actions;
    private final DialogService dialogService;


    public DialogManager(List<BotAction> actions, DialogService dialogService) {
        this.actions = actions;
        this.dialogService = dialogService;
    }

    public UserDialog getDialog(long userId) {
        return  dialogs.computeIfAbsent(userId, k -> new UserDialog(userId));
    }

    public BotAction getAction(Update update) {
        if (update == null) {
            throw new NullPointerException("Update is null"); //TODO: класс ошибки
        }
        UserDialog dialog = getDialog(dialogService.getUserId(update));
        dialog.setChatId(dialogService.getChatId(update));
        BotAction defaultAction = actions.stream().filter(a -> "MessageSent".equals(a.getName())).findFirst().orElseThrow();
        for (BotAction action : actions) {
            if (!action.getName().equals(defaultAction.getName()) && action.canHandle(update)) {
                return action;
            }
        }

        return defaultAction;
    }

}
