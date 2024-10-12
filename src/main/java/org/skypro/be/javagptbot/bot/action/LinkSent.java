package org.skypro.be.javagptbot.bot.action;

import org.skypro.be.javagptbot.github.GithubService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;

public class LinkSent implements BotAction {

    private final long chatId;
    private final String link;
    private GithubService gitHubService = new GithubService();

    public LinkSent(long chatId, String link) {
        this.chatId = chatId;
        this.link = link;
    }

    @Override
    public SendMessage getAnswer() {
        Map<String, String> projectFiles = new HashMap<>();
        return SendMessage.builder()
                .chatId(chatId)
                .text(LINK_MESSAGE)
                .build();
    }

    private static final String LINK_MESSAGE = "Вы прислали ссылку на пулл-реквест!";
}
