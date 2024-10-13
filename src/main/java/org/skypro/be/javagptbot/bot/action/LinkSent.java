package org.skypro.be.javagptbot.bot.action;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.skypro.be.javagptbot.bot.exception.GithubAuthenticationException;
import org.skypro.be.javagptbot.bot.exception.InvalidPullRequestLinkException;
import org.skypro.be.javagptbot.github.GithubService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Data
public class LinkSent implements BotAction {

    private long chatId;
    private String link;
    private final GithubService gitHubService;

    public LinkSent(GithubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Override
    public SendMessage getAnswer() {
        Map<String, String> projectFiles = new HashMap<>();
        String messageText = "";
        try {
            projectFiles = gitHubService.getProjectFiles(link);
            messageText = projectFiles.keySet().toString();
        } catch (InvalidPullRequestLinkException | GithubAuthenticationException e) {
            messageText = e.getMessage();
        } catch (IOException e) {
            log.error(e.getMessage());
            messageText = "Unknown error occurred. Please try again later";
        }

        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .build();
    }

    @Override
    public boolean canHandle(Update update) {
        if (update.hasMessage()
                && update.getMessage().getText().startsWith("https://github.com/")
                && update.getMessage().getText().contains("pull")) {
            link = update.getMessage().getText();
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "LinkSent";
    }

    private static final String LINK_MESSAGE = "Вы прислали ссылку на пулл-реквест!";
}
