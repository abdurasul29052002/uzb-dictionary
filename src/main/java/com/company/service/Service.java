package com.company.service;

import com.company.entity.Words;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Service {

    @SneakyThrows
    public StringBuilder buildString(StringBuilder stringBuilder, String key, String data){
        stringBuilder.append(key+"="+URLEncoder.encode(data,"UTF-8"));
        stringBuilder.append("&");
        return stringBuilder;
    }

    @SneakyThrows
    public InputStream postConnection(String stringUrl, String json){
        URL url = new URL(stringUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("POST");
        PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream(), true);
        printWriter.println(json);
        printWriter.close();
        return httpURLConnection.getInputStream();
    }

    public ReplyKeyboardMarkup getMainKeyboard(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.addAll(Arrays.asList("So'z qidirish","Admin bilan bog'lanish"));
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.addAll(Arrays.asList("Yordam","Sozlamalar"));
        replyKeyboardMarkup.setKeyboard(Arrays.asList(keyboardRow1,keyboardRow2));
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public InlineKeyboardMarkup getWordsInlineKeyboard(Words words){
        List<List<InlineKeyboardButton>> inlineKeyboardRowList = new ArrayList<>();
        String[] suggestions = words.getSuggestions();
        List<InlineKeyboardButton> inlineKeyboardRow = new ArrayList<>();
        int numberOfButtons = 0;
        for (String suggestion : suggestions) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(suggestion);
            inlineKeyboardButton.setCallbackData(suggestion);
            inlineKeyboardRow.add(inlineKeyboardButton);
            numberOfButtons++;
            if (numberOfButtons == 2) {
                inlineKeyboardRowList.add(inlineKeyboardRow);
                inlineKeyboardRow = new ArrayList<>();
                numberOfButtons = 0;
            }
        }
        return new InlineKeyboardMarkup(inlineKeyboardRowList);
    }
}
