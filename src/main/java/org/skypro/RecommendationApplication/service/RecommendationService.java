package org.skypro.RecommendationApplication.service;

import org.skypro.RecommendationApplication.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecommendationService {

    RecommendationsRepository recommendationsRepository;

    public RecommendationService(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    public void test(UUID uuid) {
        System.out.println(recommendationsRepository.getRandomTransactionAmount(uuid));
    }
}
