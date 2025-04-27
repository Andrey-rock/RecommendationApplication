package org.skypro.RecommendationApplication.controller;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.DTO.Response;
import org.skypro.RecommendationApplication.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class RecommendationController {

    RecommendationService recommendationService;
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendation/{id}")
    public Response getRecommendations (@PathVariable UUID id) {
        List<RecommendationDTO> recommendations = recommendationService.getRecommendations(id);
        return new Response(id, recommendations);
    }
}
