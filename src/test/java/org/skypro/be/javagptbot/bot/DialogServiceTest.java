/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.be.javagptbot.TestsDataSet;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension .class)
class DialogServiceTest {

    @Mock
    Update update;

    @Mock
    SendMessage sendMessage;

    @Mock
    Message message;

    @Mock
    CallbackQuery callbackQuery;

    @Mock
    User user;

    @InjectMocks
    DialogService out;

    @Test
    void getChatId_testIfHasMessage() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123456789L);
        assertEquals(123456789L, out.getChatId(update));
    }

    @Test
    void getChatId_testIfHasCallbackQuery() {
        when(update.hasMessage()).thenReturn(false);
        when(update.hasCallbackQuery()).thenReturn(true);
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(123456789L);
        assertEquals(123456789L, out.getChatId(update));
    }

    @Test
    void getChatId_testWhenHasNotMessageOrCallbackQuery() {
        when(update.hasMessage()).thenReturn(false);
        when(update.hasCallbackQuery()).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> out.getChatId(update));
    }


    @Test
    void getUserId_testIfHasMessage() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(123456789L);
        assertEquals(123456789L, out.getUserId(update));
    }

    @Test
    void getUserId_testIfHasCallbackQuery() {
        when(update.hasMessage()).thenReturn(false);
        when(update.hasCallbackQuery()).thenReturn(true);
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(123456789L);
        assertEquals(123456789L,out.getUserId(update));
    }

    @Test
    void getUserId_testIfHasNotMessageOrCallbackQuery() {
        when(update.hasMessage()).thenReturn(false);
        when(update.hasCallbackQuery()).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> out.getUserId(update));
    }


    @Test
    void getMessages_testWhenMessageHasLongText() {
        String longMessageToSend = TestsDataSet.getLongTextMessage(5000);
        when(sendMessage.getText()).thenReturn(longMessageToSend);
        when(sendMessage.getChatId()).thenReturn("123456789");
        List<SendMessage> actual = out.getMessages(sendMessage);

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    void getMessages_testWhenMessageHasShortText() {
        String longMessageToSend = TestsDataSet.getLongTextMessage(100);
        when(sendMessage.getText()).thenReturn(longMessageToSend);
        when(sendMessage.getChatId()).thenReturn("123456789");
        List<SendMessage> actual = out.getMessages(sendMessage);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(longMessageToSend,actual.get(0).getText());
    }

}