package org.skypro.RecommendationApplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.DTO.Response;
import org.skypro.RecommendationApplication.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Получение рекомендаций по id пользователя на основе данных о предыдущих транзакциях.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Tag(name = "Рекомендации для пользователя")
@RestController
public class RecommendationController {

    private final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    RecommendationService recommendationService;
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Метод получения списка рекомендуемых продуктов для пользователя по его id.
     *
     * @param id Идентификатор пользователя.
     * @return Список рекомендуемых продуктов.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить рекомендации")
    @GetMapping("/recommendation/{id}")
    public Response getRecommendations (@PathVariable UUID id) {

        logger.debug("Start get recommendations for {}", id);
        List<RecommendationDTO> recommendations = recommendationService.getRecommendations(id);

        logger.debug("End get recommendations for {}", id);
        return new Response(id, recommendations);
    }
}
