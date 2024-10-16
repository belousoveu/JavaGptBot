package org.skypro.be.javagptbot.bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GptBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient client;
    private final DialogService dialogService;
    private final DialogManager dialogManager;
    private boolean blocked = false;


    public GptBot(DialogService dialogService, DialogManager dialogManager) {
        this.dialogService = dialogService;
        this.dialogManager = dialogManager;
        this.client = new OkHttpTelegramClient(getBotToken());
    }

    @PostConstruct
    public void setBotCommands() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("start", "Запуск бота"));
        commands.add(new BotCommand("help", "Помощь по работе бота"));

        try {
            client.execute(new SetMyCommands(commands));
            log.info("{} commands added", commands.size());
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return String.valueOf(System.getenv("BOT_TOKEN"));
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        long userId = dialogService.getUserId(update);
        UserDialog dialog = dialogManager.getDialog(userId);
        if (!dialog.isBlocked()) {
            dialog.setBlocked(true);
            try {
                SendMessage message = dialogManager.getAction(update).getAnswer(dialog);
                client.execute(message);
            } catch (TelegramApiException  e) {
                log.error(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                dialog.setBlocked(false);
            }
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("Registered bot running state is: {}", botSession.isRunning());
    }
}
