/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Slf4j
public class TextUtils {

    public static String convertToString(InputStream inputStream) {
        String result="";
        try (Scanner s = new Scanner(inputStream).useDelimiter("\\A")) {
            result = s.hasNext() ? s.next() : "";
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
        return result;
    }

    public static String convertToString(Map<String, String> map) {
        return map.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "\n" + entry.getValue())
                .collect(Collectors.joining("\n"));
    }
}
