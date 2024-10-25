/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaGptBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaGptBotApplication.class, args);
    }

}

// TODO: мысли на будущее:
/// 0. Сделать рефакторинг - API клиента вынести в интерфейс.
/// 0.1 Дописать тесты
/// 1. Сделать режим чата и режим тьютора. В режиме чата - общий диалог с моделью. Режим тьютора - анализ кода проекта
/// 2. Выводить ответ модели в разметке Markdown.
/// 3. Сделать анализ проекта не только по ссылке на пулл-реквест, но и по ссылке на репозиторий.
///