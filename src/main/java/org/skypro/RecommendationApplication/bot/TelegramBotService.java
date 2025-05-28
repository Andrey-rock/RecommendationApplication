package org.skypro.RecommendationApplication.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.exeption.UserNotFoundException;
import org.skypro.RecommendationApplication.model.User;
import org.skypro.RecommendationApplication.repository.RecommendationsRepository;
import org.skypro.RecommendationApplication.service.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Сервисный класс телеграм-бота. Инкапсулирует бизнес-логику.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Service
public class TelegramBotService {

    private final TelegramBot bot;

    private final RecommendationsRepository recommendationsRepository;

    private final RecommendationService recommendationService;


    public TelegramBotService(TelegramBot bot, RecommendationsRepository recommendationsRepository, RecommendationService recommendationService) {
        this.bot = bot;
        this.recommendationsRepository = recommendationsRepository;
        this.recommendationService = recommendationService;
    }

    /**
     * Метод выдачи рекомендаций по нику пользователя.
     *
     * @param chatId Идентификатор чата в который следует посылать ответ.
     * @param username Ник пользователя.
     * @throws UserNotFoundException, если пользователь с заданным ником не найден в БД.
     */
    public void getRecommendations(Long chatId, String username) throws UserNotFoundException {
        User user = recommendationsRepository.getUserByUsername(username);
        UUID id = user.getId();
        String first_name = user.getFirst_name();
        String last_name = user.getLast_name();
        List<RecommendationDTO> recommendations = recommendationService.getRecommendations(id);
        bot.execute(new SendMessage(chatId, "Здравствуйте, " + first_name + " " + last_name + "\n" +
                "Новые продукты для вас:\n"));
        for (RecommendationDTO recommendation : recommendations) {
            String name = recommendation.getName();
            String text = recommendation.getText();
            bot.execute(new SendMessage(chatId, name + ".\n" + text + "\n"));
        }
    }
}
