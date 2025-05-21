package org.skypro.RecommendationApplication.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.skypro.RecommendationApplication.exeption.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private TelegramBotService telegramBotService;

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
                String text = "Привет, " + userName + "!\nЯ бот для получения рекомендаций.\nВведи команду " +
                        "/recommend username";
                telegramBot.execute(new SendMessage(chatId, text));
            } else if (usersText.startsWith("/recommend")) {
                try {
                    String username = usersText.substring("/recommend".length() + 1);
                    telegramBotService.getRecommendations(chatId, username);
                } catch (UserNotFoundException e) {
                    telegramBot.execute(new SendMessage(chatId, e.getMessage()));
                } catch (Exception e) {
                    telegramBot.execute(new SendMessage(chatId, "Что-то пошло не так \uD83D\uDE15 Повторите команду"));
                }
            } else {
                telegramBot.execute(new SendMessage(chatId, "команда не распознана"));
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
