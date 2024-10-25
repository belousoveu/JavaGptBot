/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.utils;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.skypro.be.javagptbot.TestsDataSet.MAP_TO_STRING;
import static org.skypro.be.javagptbot.TestsDataSet.TEST_DATA;

class TextUtilsTest {

    @Test
    void convertToString_withInputStreamParam() {
        InputStream inputStream = new ByteArrayInputStream("Testing".getBytes());
        String result = TextUtils.convertToString(inputStream);
        assertEquals("Testing", result);
    }

    @Test
    void convertToString_withInputStreamParamIsNull() {
        String result = TextUtils.convertToString((InputStream) null);
        assertEquals("", result);
    }


    @Test
    void testConvertToString_withMapParam() {
        assertEquals(MAP_TO_STRING, TextUtils.convertToString(TEST_DATA));
    }


    @Test
    void getPart_testWhenTextLessThanMaxMessageLength() {
        assertEquals(MAP_TO_STRING, TextUtils.getPart(MAP_TO_STRING,100));
    }

    @Test
    void getPart_testWhenPartLessThanMaxMessageLength() {
        assertEquals("key1\nvalue1\n", TextUtils.getPart(MAP_TO_STRING,15));
    }

    @Test
    void getPart_testWhenPartGreaterThanMaxMessageLength() {
        assertEquals("ke", TextUtils.getPart(MAP_TO_STRING,2));
    }

}