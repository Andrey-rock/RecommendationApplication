package org.skypro.RecommendationApplication.controller;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.DTO.Response;
import org.skypro.RecommendationApplication.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class RecommendationController {

    private final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    RecommendationService recommendationService;
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendation/{id}")
    public Response getRecommendations (@PathVariable UUID id) {

        logger.info("Start get recommendations for {}", id);
        List<RecommendationDTO> recommendations = recommendationService.getRecommendations(id);

        logger.info("End get recommendations for {}", id);
        return new Response(id, recommendations);
    }
}
