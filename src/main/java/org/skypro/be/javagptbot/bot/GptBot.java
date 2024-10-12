package org.skypro.be.javagptbot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class GptBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final Logger logger = LoggerFactory.getLogger(GptBot.class);
    private final TelegramClient client;


    public GptBot() {
        this.client = new OkHttpTelegramClient(getBotToken());

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            SendMessage message = SendMessage
                    .builder()
                    .chatId(chat_id)
                    .text(message_text)
                    .build();
            try {
                client.execute(message);
            } catch (TelegramApiException e) {
                logger.error(e.getMessage());
            }
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        logger.info("Registered bot running state is: {}", botSession.isRunning());
    }
}
