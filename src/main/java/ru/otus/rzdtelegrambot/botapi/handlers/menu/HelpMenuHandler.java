package ru.otus.rzdtelegrambot.botapi.handlers.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.otus.rzdtelegrambot.botapi.BotState;
import ru.otus.rzdtelegrambot.botapi.RZDTelegramBot;
import ru.otus.rzdtelegrambot.botapi.handlers.InputMessageHandler;
import ru.otus.rzdtelegrambot.service.MainMenuService;
import ru.otus.rzdtelegrambot.service.ReplyMessagesService;

import java.util.Arrays;
import java.util.List;


@Component
public class HelpMenuHandler implements InputMessageHandler {
    private MainMenuService mainMenuService;
    private ReplyMessagesService messagesService;

    public HelpMenuHandler(MainMenuService mainMenuService, ReplyMessagesService messagesService) {
        this.mainMenuService = mainMenuService;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        //return mainMenuService.getMainMenuMessage(message.getChatId(),
        //       messagesService.getEmojiReplyText("reply.helpMenu.welcomeMessage", Emojis.HELP_MENU_WELCOME));

//      Testing youtube
        SendMessage mes = new SendMessage();
        mes.setChatId(message.getChatId());
        mes.setText("https://www.youtube.com/watch?v=kgyLKGTkaaA");

        return mes;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_HELP_MENU;
    }
}
