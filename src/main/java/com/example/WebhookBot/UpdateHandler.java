package com.example.WebhookBot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class UpdateHandler {
    TelegramBot telegramBot;

    public UpdateHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }



    public void handleMessage(Message message){
        long chatId = message.getChatId();
        if (message.hasText()){
            String messageText = message.getText();
            if (messageText.equals("/start")){
                sendMessage(chatId,"Hello, "+message.getChat().getUserName());
            } else if (messageText.equals("/menu")) {
                sendMessageWithButtons(chatId,"Btts",new String[][]{{"1","2"},{"3","4"}},new String[][]{{"b1", "b2"}, {"b3", "b4"}});
            }

        }else if (message.hasPhoto()){

        }
    }

    public void handleCallbackQuery(CallbackQuery callbackQuery){
       String callbackData = callbackQuery.getData();
       long chatId = callbackQuery.getMessage().getChatId();
        if (callbackData.equals("b1")){
            sendMessage(chatId,"You pressed 1 button");
        } else if (callbackData.equals("b2")) {
            sendMessage(chatId,"You pressed 2 button");
        }else if (callbackData.equals("b3")) {
            sendMessage(chatId,"You pressed 3 button");
        }else if (callbackData.equals("b4")) {
            sendMessage(chatId,"You pressed 4 button");
        }
    }
    public void sendMessage(long chatId, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.err.println(e.getMessage());
        }
    }
    public void sendMessageWithButtons(long chatId, String text, String[][] buttonsName, String[][] buttonsCallbackData) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        for (int i = 0; i < buttonsName.length; i++) {
            List<InlineKeyboardButton> btns = new ArrayList<>();
            for (int j = 0; j < buttonsName[i].length; j++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(buttonsName[i][j]);
                button.setCallbackData(buttonsCallbackData[i][j]);
                btns.add(button);
            }
            inlineKeyboardButtons.add(btns);
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {

        }
    }
}
