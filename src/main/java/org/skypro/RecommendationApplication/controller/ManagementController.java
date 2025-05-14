package org.skypro.RecommendationApplication.controller;


import org.skypro.RecommendationApplication.repository.RecommendationsRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/management")
public class ManagementController {

    private final RecommendationsRepository recommendationsRepository;

    public ManagementController(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @PostMapping("/clear-caches")
    public void clearCaches() {
        recommendationsRepository.clearCash();
    }


}
