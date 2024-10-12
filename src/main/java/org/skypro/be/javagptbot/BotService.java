package org.skypro.be.javagptbot;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Data
public class BotService {


    public String getMessageText(Update update) {
        return update.hasMessage() ? update.getMessage().getText() : "";
    }
}
