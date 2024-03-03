package com.example.WebhookBot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.io.IOException;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBot extends SpringWebhookBot {
    String botPath;
    String botUsername;
    String botToken;
    UpdateHandler updateHandler;



    public TelegramBot(SetWebhook setWebhook) {
        super(setWebhook);
        updateHandler = new UpdateHandler(this);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (IllegalArgumentException e) {
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "errorillegal");
        } catch (Exception e) {
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "error");
        }
    }

    private BotApiMethod<?> handleUpdate(Update update) throws IOException {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

        } else {
            Message message = update.getMessage();
            if (message != null) {
                updateHandler.handleMessage(message);
            }
        }
        return null;
    }
}
