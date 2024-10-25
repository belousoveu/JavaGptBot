# Java Code Analysis Bot

## Краткое описание

Простой бот, который анализирует небольшие Java-проекты на соответствие принципам ООП, SOLID, KISS, YAGNI, DRY. Проверяет правильность применения шаблонов проектирования и выдает рекомендации по оптимизации.

## Дополнительная информация

- **AI Модель**: Проект использует API GigaChat для формирования ответов.
- **Интеграция с GitHub**: Бот получает доступ к пулл-реквестам публичных репозиториев GitHub для получения файлов проекта для анализа.
- **Подробные объяснения**: Бот подробно разъясняет, как правильно применять принципы ООП, SOLID, KISS, YAGNI, DRY, шаблоны проектирования и дает рекомендации по оптимизации анализируемого проекта с учетом этих принципов.

## Установка и настройка

### Предварительные требования

- Java Development Kit (JDK) 17 или выше
- Maven
- Доступ к API GigaChat
- Токен GitHub
- Токен бота

### Конфигурация

1. **Настройка токенов**:
   Добавьте следующие токены в файл `application.properties`:
   ```properties
   giga.chat.authorization.key=ваш_ключ_giga_chat
   github.token=ваш_токен_github
   bot.token=ваш_токен_бота
   ```

2. **Сборка проекта**:
   Скомпилируйте проект в JAR файл с помощью Maven:
   ```bash
   mvn clean install
   ```

3. **Размещение промтов на сервере**:
   Поместите файлы промтов в директорию `/opt/tgbot/prompt/` на вашем сервере:
   - `question.txt`: Системный промпт для ответа на вопросы
   - `questions.txt`: Промпт пользователя для формирования списка вопросов
   - `student.txt`: Системный промпт для формирования списка вопросов
   - `task.txt`: Промпт пользователя для анализа проекта
   - `tutor.txt`: Системный промпт для анализа проекта
Примеры промптов приведены в папке проекта : `src/main/resources/prompt`

### Запуск бота

1. **Развертывание JAR файла**:
   Разверните скомпилированный JAR файл на вашем сервере.

2. **Запуск бота**:
   Запустите JAR файл:
   ```bash
   java -jar target/java-code-analysis-bot.jar
   ```

## Использование

### Анализ проекта

1. **Доступ к боту**:
   Используйте бота для анализа Java-проекта, предоставив URL репозитория GitHub или ссылку на пулл-реквест.

2. **Получение анализа**:
   Бот проанализирует проект и предоставит подробную обратную связь по:
   - Соответствию принципам ООП, SOLID, KISS, YAGNI, DRY
   - Правильному применению шаблонов проектирования
   - Рекомендациям по оптимизации

### Пример взаимодействия

![photo_2024-10-20_00-22-52](https://github.com/user-attachments/assets/58efdcf5-34d7-420a-b509-f32c683eae1d)
![photo_2024-10-20_00-22-52 (2)](https://github.com/user-attachments/assets/3006cf5a-6340-436d-b867-43850d87f3a9)


## Лицензия

Этот проект лицензирован под [MIT License](LICENSE).

---

**Свяжитесь с нами**: Если у вас есть вопросы или предложения, пожалуйста, свяжитесь с нами через [GitHub Issues](https://github.com/belousoveu/JavaGptBot/issues) или напрямую через электронную почту.

---

**Бот доступен по адресу**: [https://t.me/JavaLearningZmBot](https://t.me/JavaLearningZmBot)

**Спасибо за интерес к нашему проекту!**