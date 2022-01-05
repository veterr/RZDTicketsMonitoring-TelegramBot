package ru.otus.rzdtelegrambot.botapi.handlers;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.otus.rzdtelegrambot.botapi.BotState;

/**
 * @author Sergei Viacheslaev
 */
public interface InputMessageHandler {
    SendMessage handle(Message message);

    BotState getHandlerName();
}
