package org.skypro.RecommendationApplication.controller;


import org.skypro.RecommendationApplication.DTO.Info;
import org.skypro.RecommendationApplication.repository.RecommendationsRepository;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/management")
public class ManagementController {

    private final BuildProperties properties;

    private final RecommendationsRepository recommendationsRepository;

    public ManagementController(BuildProperties properties, RecommendationsRepository recommendationsRepository) {
        this.properties = properties;
        this.recommendationsRepository = recommendationsRepository;
    }

    @PostMapping("/clear-caches")
    public void clearCaches() {
        recommendationsRepository.clearCash();
    }

    @GetMapping("/info")
    public Info info() {
        return new Info(properties.getName(), properties.getVersion());
    }
}
