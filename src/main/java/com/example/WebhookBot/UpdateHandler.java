package com.example.WebhookBot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class UpdateHandler {
    TelegramBot telegramBot;

    public UpdateHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }



    public void handleMessage(Message message){
        long chatId = message.getChatId();
        if (message.hasText()){
            String messageText = message.getText();
            switch (messageText){
                case "/start":
                    sendMessage(chatId,"Start!");
                    break;
                case "/profile":
                    sendMessage(chatId,"is not ready yet");
                    break;
                default:
                    sendMessage(chatId,messageText);

            }
        }else {

        }
    }

    public void handleCallbackQuery(CallbackQuery callbackQuery){

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
}
