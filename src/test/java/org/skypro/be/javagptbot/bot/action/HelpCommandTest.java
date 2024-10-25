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
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class HelpCommandTest {

    @Mock
    UserDialog userDialog;

    @Mock
    Update update;

    @Mock
    Message message;

    @InjectMocks
    HelpCommand out;

    @Test
    void getAnswer_test() {
        when(userDialog.getChatId()).thenReturn(1L);
        SendMessage actual = out.getAnswer(userDialog);
        assertNotNull(actual);
        assertEquals("1", actual.getChatId());
        assertEquals(TestsDataSet.HELP_MESSAGE, actual.getText());
    }

    @Test
    void canHandle_testWhenUpdateHasMessageAndTextIsHelp() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("/help");
        assertTrue(out.canHandle(update));
    }

    @Test
    void canHandle_testWhenUpdateHasMessageAndTextNotHelp() {
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
        assertEquals("HelpCommand", out.getName());
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