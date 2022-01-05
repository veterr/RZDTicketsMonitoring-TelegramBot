package ru.otus.rzdtelegrambot.botapi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RZDTelegramBot extends TelegramWebhookBot {
    String botPath;
    String botUsername;
    String botToken;
    String paymentToken;

    private TelegramFacade telegramFacade;

    public RZDTelegramBot(DefaultBotOptions options, TelegramFacade telegramFacade) {
        super(options);
        this.telegramFacade = telegramFacade;
    }

    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if ("/help".contains(update.getMessage().getText())) {
            this.execute(sendInvoice(update.getMessage().getChatId()));
            //this.execute(sendAudio(update));
            //this.execute(sendVideo(update));
            //this.execute(sendSticker(update));
        }
        SendMessage replyMessageToUser = telegramFacade.handleUpdate(update);

        return replyMessageToUser;
    }

    private SendSticker sendSticker(Update update) {
        SendSticker sticker = new SendSticker();
        sticker.setChatId(update.getMessage().getChatId());
        sticker.setSticker(new File("D:\\users\\dmitr\\IdeaProjects\\bm\\RZDTicketsMonitoring-TelegramBot\\src\\main\\resources\\0.png"));
        return sticker;
    }

    private SendVideo sendVideo(Update update) {
        SendVideo vid = new SendVideo();
        vid.setChatId(update.getMessage().getChatId());
        vid.setVideo(new File(
                "D:\\users\\dmitr\\IdeaProjects\\bm\\RZDTicketsMonitoring-TelegramBot\\src\\main\\resources\\Life_is_Beautifull_Life_is_A_Great_Lesson.mp4"));
        return vid;
    }

    private SendAudio sendAudio(Update update) {
        SendAudio aud = new SendAudio();
        aud.setChatId(update.getMessage().getChatId());
        aud.setAudio(new InputFile(new File(
                "D:\\users\\dmitr\\IdeaProjects\\bm\\RZDTicketsMonitoring-TelegramBot\\src\\main\\resources\\file_example_MP3_700KB.mp3"),
                "Cool sample!"));
        return aud;
    }

    public void sendMessage(long chatId, String textMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textMessage);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendInlineKeyBoardMessage(long chatId, String messageText, String buttonText, String callbackData) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton().setText(buttonText);

        if (callbackData != null) {
            keyboardButton.setCallbackData(callbackData);
        }

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(keyboardButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        try {
            execute(new SendMessage().setChatId(chatId).setText(messageText).setReplyMarkup(inlineKeyboardMarkup));
            //execute(sendInvoice(chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendAnswerCallbackQuery(String callbackId, String message) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackId);
        answer.setText(message);
        answer.setShowAlert(true);

        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendChangedInlineButtonText(CallbackQuery callbackQuery, String buttonText, String callbackData) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        final List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        final long message_id = callbackQuery.getMessage().getMessageId();
        final long chat_id = callbackQuery.getMessage().getChatId();
        final List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(buttonText).setCallbackData(callbackData));
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);

        EditMessageText editMessageText = new EditMessageText().setChatId(chat_id).setMessageId((int) (message_id)).
                setText(callbackQuery.getMessage().getText());

        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public SendInvoice sendInvoice(long chatId) throws TelegramApiException {
        SendInvoice invoice = new SendInvoice();
        invoice.setTitle("Guruji Lila");
        invoice.setDescription("Buy yourself some devine love!");
        invoice.setPhotoUrl("https://u.livelib.ru/reader/IrinaKondratovich/o/otl4d1gl/o-o.jpeg");
        invoice.setPhotoHeight(300);
        invoice.setPhotoWidth(300);
        invoice.setStartParameter("startParam");
        invoice.setCurrency("RUB");
        invoice.setProviderToken(paymentToken);
        invoice.setChatId((int) chatId);
        invoice.setNeedEmail(true);
        invoice.setNeedName(true);
        invoice.setPrices(Arrays.asList(new LabeledPrice("Сам билет", 100*100), new LabeledPrice("Секс с проводницей", 50*100)));
        invoice.setPayload("lila:5hjjk78kll694fg");
        InlineKeyboardMarkup keyBoard = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonBuy = new InlineKeyboardButton();
        buttonBuy.setText("Будем куплять!");
        buttonBuy.setPay(true);
        InlineKeyboardButton buttonNotBuy = new InlineKeyboardButton();
        buttonNotBuy.setText("Покупать не будем!");
        buttonNotBuy.setCallbackData("No_buy");
        List<List<InlineKeyboardButton>> buttons = Arrays.asList(Arrays.asList(buttonBuy), Arrays.asList(buttonNotBuy));
        keyBoard.setKeyboard(buttons);
        invoice.setReplyMarkup(keyBoard);
        return invoice;
    }

    public class PayMenuDto {
        public PayMenuDto() {

        }
    }
}

