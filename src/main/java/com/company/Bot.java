package com.company;

import com.company.entity.*;
import com.company.service.Service;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "uzbek_dictionary_bot";
    }

    @Override
    public String getBotToken() {
        return "5352284183:AAEJrtBsA-SC86Slj4mfi-xghzZYqHywuvE";
    }

    Service service = new Service();

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Update received");
        String textOfMessage;
        Long chatId;
        Gson gson = new Gson();
        SendMessage sendMessage = new SendMessage();
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            textOfMessage = update.getMessage().getText();
            sendMessage.setChatId(String.valueOf(chatId));
            switch (textOfMessage){
                case "/start":{
                    sendMessage.setText("Xush kelibsiz");
                    sendMessage.setReplyMarkup(service.getMainKeyboard());
                    execute(sendMessage);
                    break;
                }
                case "So'z qidirish":{
                    sendMessage.setText("Qidirmoqchi bo'lgan so'zingizni yozing");
                    execute(sendMessage);
                    break;
                }
                case "":{

                    break;
                }
                case "Yordam":{

                    break;
                }
                case "Sozlamalar":{

                    break;
                }
                default:{
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("keyword=").append(URLEncoder.encode(textOfMessage, "UTF-8"));
                    stringBuilder.append("&");
                    stringBuilder.append("names=").append(URLEncoder.encode("false", "UTF-8"));
                    InputStream inputStream = service.postConnection("https://savodxon.uz/api/search", stringBuilder.toString());
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    Words words = gson.fromJson(inputStreamReader, Words.class);
                    if (!words.isMatchFound()) {
                        sendMessage.setText("Bu so`z bo`yicha hech qanaqa natija topilmadi");
                        execute(sendMessage);
                    }
                    sendMessage.setReplyMarkup(service.getWordsInlineKeyboard(words));
                    sendMessage.setText("Bizda mavjud bo`lgan variantlar");
                    execute(sendMessage);
                }
            }
        }
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            chatId = callbackQuery.getMessage().getChatId();
            String data = callbackQuery.getData();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("word=" + URLEncoder.encode(data, "UTF-8"));
            InputStream inputStream = service.postConnection("https://savodxon.uz/api/get_definition", stringBuilder.toString());
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            Core core = gson.fromJson(inputStreamReader, Core.class);
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setParseMode(ParseMode.HTML);
            StringBuilder sendMessageBuilder = new StringBuilder();
            sendMessageBuilder.append("Siz tanlagan so`z uchun quyidagicha izoh topildi");
            sendMessageBuilder.append("\n<b>" + data.toUpperCase() + "</b>");
            for (Definition definition : core.getDefinition()) {
                System.out.println(definition.getTags());
                for (Meanings meaning : definition.getMeanings()) {
                    sendMessageBuilder.append("\n<b>" + meaning.getTags() + "</b> <u>" + meaning.getText() + "</u>");
                    Examples[] examples = meaning.getExamples();
                    for (int i = 1; i <= examples.length; i++) {
                        sendMessageBuilder.append("\n" + i + ". " + examples[i - 1].getText() + (!examples[i - 1].getTakenFrom().equals("") ? "(" + examples[i - 1].getTakenFrom() + ")" : " "));
                    }
                }
            }
            sendMessage.setText(sendMessageBuilder.toString());
            execute(sendMessage);
        }
    }
}
