package org.skypro.RecommendationApplication.controller;

import org.skypro.RecommendationApplication.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RecommendationController {

    UUID id = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409540b");

    RecommendationService recommendationService;
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/test/{id}")
    public void test(@PathVariable UUID id) {
        recommendationService.test(id);
    }
}
