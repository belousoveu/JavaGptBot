/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class TextUtils {

    public static final String PROMPTS_PATH = "src/main/resources/prompt/";

    public static String convertToString(InputStream inputStream) {
        String result = "";
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

    public static String getPrompt(String prompt) {
        String fileName = PROMPTS_PATH + prompt + ".txt";
        System.out.println("fileName = " + fileName);
        try {
            String content = Files.readString(Paths.get(fileName));
            return content.replace("\\R", " ");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return "";
    }

    public static String getPart(String text, int maxMessageLength) {
        if (text.length() <= maxMessageLength) {
            return text;
        }
        String[] parts = text.split("\n");
        StringBuilder result = new StringBuilder();
        int i = 0;
        do {
            result.append(parts[i]).append("\n");
            i++;
        } while (i < parts.length && result.length() + parts[i].length() < maxMessageLength);

        if (result.length() > maxMessageLength) {
            result.setLength(maxMessageLength);
        }
        return result.toString();
    }


    public static String correctMarkdownFormat(String text) {

        //text = escapeUnbalancedMarkdown(text);

       // text = escapeInvalidMarkdownElements(text);

        return text;
//                .replace("**", "");
    }

    private static String escapeUnbalancedMarkdown(String text) {
        String[] markdownSymbols = {"\\*", "\\_", "`", "\\[", "\\]"};
        for (String symbol : markdownSymbols) {
            Pattern pattern = Pattern.compile(symbol);
            Matcher matcher = pattern.matcher(text);
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            if (count % 2 != 0) {
                text = text.replaceAll(symbol, "\\\\" + symbol.substring(1));
            }
        }
        return text;
    }

    private static String escapeInvalidMarkdownElements(String text) {
        String[] markdownPatterns = {
                "\\*\\*[^\\*]+\\*\\*", // Жирный текст
                "\\_\\_[^\\_]+\\_\\_", // Курсив
                "`[^`]+`",             // Встроенный код
                "```[^`]+```"          // Блок кода
        };

        for (String pattern : markdownPatterns) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(text);
            while (m.find()) {
                String match = m.group();
                if (!isValidMarkdownElement(match)) {
                    text = text.replace(match, escapeMarkdown(match));
                }
            }
        }
        return text;

    }

    private static boolean isValidMarkdownElement(String element) {
        String[] markdownSymbols = {"\\*", "\\_", "`", "\\[", "\\]"};
        for (String symbol : markdownSymbols) {
            if (element.contains(symbol)) {
                return false;
            }
        }
        return true;
    }

    private static String escapeMarkdown(String text) {
        // Экранируем символы разметки
        String[] markdownSymbols = {"\\*", "\\_", "`", "\\[", "\\]"};
        for (String symbol : markdownSymbols) {
            text = text.replaceAll(symbol, "\\\\" + symbol.substring(1));
        }
        return text;

    }

    public static List<String> getQuestionsList(String answer) {
        return Arrays.stream(answer.split("\n")).toList();
    }
}
