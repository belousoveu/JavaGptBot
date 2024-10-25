/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.skypro.be.javagptbot.bot.action.BotAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GptBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private TelegramClient client;
    private final DialogService dialogService;
    private final DialogManager dialogManager;

    @Value("${bot.token}")
    private String botToken;


    public GptBot(DialogService dialogService, DialogManager dialogManager) {
        this.dialogService = dialogService;
        this.dialogManager = dialogManager;

    }

    @PostConstruct
    public void setBotCommands() {
        this.client = new OkHttpTelegramClient(getBotToken());
        //TODO вынести инициализацию команд в отдельный класс
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("start", "Начало работы с ботом"));
        commands.add(new BotCommand("help", "Помощь по работе с ботом"));

        try {
            client.execute(new SetMyCommands(commands));
            log.info("{} commands added", commands.size());
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
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
            BotAction currentAction = dialogManager.getAction(update);
            try {
                Message tempMessage = null;
                if (currentAction.isLongOperation()) {
                    tempMessage = sendTextMessage(currentAction.getLongOperationMessage(), dialog.getChatId());
                }
                SendMessage message = currentAction.getAnswer(dialog);
                List<SendMessage> messages = dialogService.getMessages(message);
                if (tempMessage != null) {
                    deleteMessage(tempMessage);
                }
                for (SendMessage msg : messages) {
                    client.execute(msg);
                }
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            } finally {
                dialog.setBlocked(false);
            }
        }
    }

    private Boolean deleteMessage(Message tempMessage) throws TelegramApiException {
        DeleteMessage deleteMessage = DeleteMessage.builder().chatId(tempMessage.getChatId()).messageId(tempMessage.getMessageId()).build();
        return client.execute(deleteMessage);
    }

    private Message sendTextMessage(String s, long chatId) throws TelegramApiException {
        SendMessage message = SendMessage.builder().text(s).chatId(chatId).build();
        return client.execute(message);
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("Registered bot running state is: {}", botSession.isRunning());
    }
}
