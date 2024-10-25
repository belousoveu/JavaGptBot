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
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.be.javagptbot.bot.UserDialog;
import org.skypro.be.javagptbot.gigachat.GigaChatApi;
import org.skypro.be.javagptbot.utils.TextUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ButtonClickTest {

    @Mock
    UserDialog userDialog;

    @Mock
    Update update;

    @Mock
    CallbackQuery callbackQuery;

    @Mock
    GigaChatApi gigaChatApi;


    @InjectMocks
    ButtonClick out;

    @Test
    void getAnswer() {
        String messageText = "answer";
        when(userDialog.getChatId()).thenReturn(1L);
        when(userDialog.getQuestionText(null)).thenReturn("");
        try (MockedStatic<TextUtils> textUtils = mockStatic(TextUtils.class)) {
            textUtils.when(() -> TextUtils.getPrompt(anyString())).thenReturn("");
        }
        when(gigaChatApi.sendQuestion(anyString(), eq(userDialog))).thenReturn(messageText);

        SendMessage actual = out.getAnswer(userDialog);

        assertEquals("1", actual.getChatId());
        assertEquals(messageText, actual.getText());


    }

    @Test
    void canHandle_testWhenHasCallbackQuery() {
        when(update.hasCallbackQuery()).thenReturn(true);
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getData()).thenReturn("ButtonClick");
        assertTrue(out.canHandle(update));
    }

    @Test
    void canHandle_testWhenHasNotCallbackQuery() {
        when(update.hasCallbackQuery()).thenReturn(false);
        assertFalse(out.canHandle(update));
    }

    @Test
    void getName_test() {
        assertEquals("ButtonClick", out.getName());
    }

    @Test
    void isLongOperation_test() {
        assertTrue(out.isLongOperation());
    }

    @Test
    void getLongOperationMessage_test() {
        assertEquals("Подождите чутка... Сейчас замучу ответ!", out.getLongOperationMessage());
    }
}