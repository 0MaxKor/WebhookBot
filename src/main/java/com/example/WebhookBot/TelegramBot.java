package com.example.WebhookBot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jsoup.Jsoup;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBot extends SpringWebhookBot {
    String botPath;
    String botUsername;
    String botToken;
    UpdateHandler updateHandler;

    Runnable refreshServerConnection = () -> {
        for (long i = 0; i < 200000000; i++) {
            System.out.println("i="+i);
            if (i>=2){

                try {
                    org.jsoup.Connection.Response response = Jsoup.connect(botPath)
                            .header("DNT", "1")
                            .header("Upgrade-Insecure-Requests", "1")
                            .header("sec-ch-ua-mobile", "?0")
                            .method(org.jsoup.Connection.Method.GET)
                            .ignoreContentType(true)
                            .execute();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                System.out.println("request "+i);

            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    };
    Thread makeRequestsToServer;

    public TelegramBot(SetWebhook setWebhook) {
        super(setWebhook);
        updateHandler = new UpdateHandler(this);
        makeRequestsToServer=new Thread(refreshServerConnection);
        makeRequestsToServer.start();
        List<BotCommand> menu = new ArrayList<>();
        menu.add(new BotCommand("/start", "Начать общение"));
        menu.add(new BotCommand("/menu", "Cписок команд бота"));
        menu.add(new BotCommand("/price", "Цены"));
        menu.add(new BotCommand("/profile", "Посмотреть профиль"));
        menu.add(new BotCommand("/help", "Написать в поддержку"));
        try {
            this.execute(new SetMyCommands(menu, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
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
                    e.getMessage());
        }
    }

    private BotApiMethod<?> handleUpdate(Update update){
        if (update.hasCallbackQuery()) {
            updateHandler.handleCallbackQuery(update.getCallbackQuery());
        } else {
            Message message = update.getMessage();
            updateHandler.handleMessage(message);
        }
        return null;
    }
}
