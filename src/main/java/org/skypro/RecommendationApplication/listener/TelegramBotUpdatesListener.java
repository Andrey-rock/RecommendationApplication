package org.skypro.RecommendationApplication.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            String usersText = update.message().text();
            long chatId = update.message().chat().id();
            String userName = update.message().chat().firstName();

            if (usersText.equals("/start")) {
                String text = "Привет, " + userName + "!\nЯ бот для получения рекомендаций.\nВведи команду" +
                        "/recommend username";
                telegramBot.execute(new SendMessage(chatId, text));
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
