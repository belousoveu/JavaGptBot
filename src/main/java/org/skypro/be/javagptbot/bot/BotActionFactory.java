package org.skypro.be.javagptbot.bot;

import org.skypro.be.javagptbot.bot.action.*;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BotActionFactory {

    public static BotAction create(Update update) {
        Handler startHandler = new StartHandler();
        Handler helpHandler = new HelpHandler();
        Handler linkHandler = new LinkHandler();
        Handler buttonHandler = new ButtonHandler();
        Handler otherHandler = new OtherHandler();

        startHandler.setNext(helpHandler);
        helpHandler.setNext(linkHandler);
        linkHandler.setNext(buttonHandler);
        buttonHandler.setNext(otherHandler);

        return startHandler.handleRequest(update);
    }

    private abstract static class Handler {
        protected Handler next;

        protected void setNext(Handler next) {
            this.next = next;
        }

        protected abstract BotAction handleRequest(Update update);
    }

    private static class StartHandler extends Handler {

        @Override
        protected BotAction handleRequest(Update update) {
            if (update.hasMessage() && update.getMessage().getText().startsWith("/start")) {
                return new StartCommand(getChatId(update));
            } else if (next != null) {
                return next.handleRequest(update);
            }
            return null;
        }
    }

    private static class HelpHandler extends Handler {

        @Override
        protected BotAction handleRequest(Update update) {
            if (update.hasMessage() && update.getMessage().getText().startsWith("/help")) {
                return new HelpCommand(getChatId(update));
            } else if (next != null) {
                return next.handleRequest(update);
            }
            return null;
        }
    }

    private static class LinkHandler extends Handler {

        @Override
        protected BotAction handleRequest(Update update) {
            if (update.hasMessage()
                    && update.getMessage().getText().startsWith("https://github.com/")
                    && update.getMessage().getText().contains("pull")) {
                return new LinkSent(getChatId(update), update.getMessage().getText());
            } else if (next != null) {
                return next.handleRequest(update);
            }
            return null;
        }
    }

    private static class ButtonHandler extends Handler {

        @Override
        protected BotAction handleRequest(Update update) {
            if (update.hasCallbackQuery()) {
                return new ButtonClick(getChatId(update), update.getCallbackQuery().getData());
            } else if (next != null) {
                return next.handleRequest(update);
            }
            return null;
        }
    }

    private static class OtherHandler extends Handler {
        @Override
        protected BotAction handleRequest(Update update) {

            return new MessageSent(getChatId(update));
        }
    }

    private static long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }
        throw new IllegalArgumentException("No chat id");
    }
}
