/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.bot.action;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.be.javagptbot.bot.UserDialog;
import org.skypro.be.javagptbot.gigachat.GigaChatApi;
import org.skypro.be.javagptbot.github.GithubService;
import org.skypro.be.javagptbot.utils.TextUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.skypro.be.javagptbot.TestsDataSet.*;

@ExtendWith(MockitoExtension.class)
class LinkSentTest {

    @Mock
    UserDialog userDialog;

    @Mock
    Update update;

    @Mock
    Message message;

    @Mock
    GithubService githubService;

    @Mock
    GigaChatApi gigaChatApi;


    @InjectMocks
    LinkSent out;


    @Test
    void getAnswer_test() throws IOException {

        String messageText = "answer";
        when(githubService.getProjectFiles(null)).thenReturn(TEST_DATA);
        try (MockedStatic<TextUtils> textUtils = Mockito.mockStatic(TextUtils.class)) {
            textUtils.when(() -> TextUtils.convertToString(TEST_DATA)).thenReturn(MAP_TO_STRING);
        }
        when(gigaChatApi.sendPullRequest(anyString(), eq(userDialog))).thenReturn(messageText);
        when(gigaChatApi.getQuestions(anyString())).thenReturn(TEST_QUESTIONS);
        when(userDialog.getChatId()).thenReturn(1L);
        doNothing().when(userDialog).setQuestions(anyMap());

        SendMessage actual = out.getAnswer(userDialog);

        assertEquals("1", actual.getChatId());
        assertEquals(messageText, actual.getText());
    }


    @Test
    void canHandle_testWhenUpdateHasMessageAndTextHasLink() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("https://github.com/pull");
        assertTrue(out.canHandle(update));

    }

    @Test
    void canHandle_testWhenUpdateHasMessageAndTextHasNotLink() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("/");
        assertFalse(out.canHandle(update));
    }

    @Test
    void canHandle_testWhenUpdateHasNotMessage() {
        when(update.hasMessage()).thenReturn(false);
        assertFalse(out.canHandle(update));
    }

    @Test
    void getName_test() {
        assertEquals("LinkSent", out.getName());
    }

    @Test
    void isLongOperation_test() {
        assertTrue(out.isLongOperation());
    }

    @Test
    void getLongOperationMessage_test() {
        assertEquals("Погоди немного... Я сейчас заценю проект и соберу ответ!", out.getLongOperationMessage());
    }
}