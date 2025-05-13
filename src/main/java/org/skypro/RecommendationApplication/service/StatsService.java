package org.skypro.RecommendationApplication.service;

import jakarta.transaction.Transactional;
import org.skypro.RecommendationApplication.model.DynamicRule;
import org.skypro.RecommendationApplication.repository.StatsRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StatsService {

    private  final StatsRepository statsRepository;

    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Transactional
    public void incrementCount(UUID dynamicRule) {
        statsRepository.incrementCount(dynamicRule);
    }
}
