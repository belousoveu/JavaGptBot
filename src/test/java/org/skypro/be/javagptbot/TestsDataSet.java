/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot;

import java.util.List;
import java.util.Map;

public class TestsDataSet {

    public static final Map<String, String> TEST_DATA = Map.of(
            "key1","value1",
            "key2","value2"
            );

    public static final List<String> TEST_QUESTIONS = List.of("question1", "question2");

    public static final String MAP_TO_STRING = "key1\nvalue1\nkey2\nvalue2";

    public static final String START_MESSAGE = """
            Эй, народ! Я бот, который может заценить ваш Java проект. 
            Проверю, насколько он в теме с ООП, чистым кодом, SOLID, KISS, DRY и YAGNI. 
            Также гляну, как используются паттерны проектирования. 
            И, конечно, подкину идеи для прокачки проекта. Готов? Погнали!
            """;
    public static final String HELP_MESSAGE = """
             Чтобы заюзать мою помощь, кинь ссылку на пулл-реквест в репозитории.
             Формат должен быть примерно такой:
             ```https://github.com/{username}/{project-name}/pull/1```
             Готово? Отправляй!
            """;
    public static final String OTHER_MESSAGE = """
            Сорян, но тут я ничем не зацеплю. Залетите еще раз в помощь - /help!
            """;


    public static String getLongTextMessage(int length) {
        return "X".repeat(length);
    }
}
