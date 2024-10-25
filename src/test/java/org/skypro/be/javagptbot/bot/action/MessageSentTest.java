/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.bot.action;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.be.javagptbot.TestsDataSet;
import org.skypro.be.javagptbot.bot.UserDialog;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageSentTest {

    @Mock
    UserDialog userDialog;

    @Mock
    Update update;

    @InjectMocks
    MessageSent out;

    @Test
    void getAnswer_test() {
        when(userDialog.getChatId()).thenReturn(1L);
        SendMessage actual = out.getAnswer(userDialog);
        assertNotNull(actual);
        assertEquals("1", actual.getChatId());
        assertEquals(TestsDataSet.OTHER_MESSAGE, actual.getText());
    }

    @Test
    void canHandle_test() {
        assertTrue(out.canHandle(update));
    }

    @Test
    void getName_test() {
        assertEquals("MessageSent", out.getName());
    }

    @Test
    void isLongOperation_test() {
        assertFalse(out.isLongOperation());
    }

    @Test
    void getLongOperationMessage_test() {
        assertEquals("", out.getLongOperationMessage());
    }
}