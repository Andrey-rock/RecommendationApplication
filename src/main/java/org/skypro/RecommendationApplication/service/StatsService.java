package org.skypro.RecommendationApplication.service;

import jakarta.transaction.Transactional;

import org.skypro.RecommendationApplication.model.Stats;
import org.skypro.RecommendationApplication.repository.StatsRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class StatsService {

    private final StatsRepository statsRepository;

    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public Collection<Stats> getStats() {
        return statsRepository.findAll();
    }

    @Transactional
    public void incrementCount(UUID dynamicRule) {
        statsRepository.incrementCount(dynamicRule);
    }

    @Transactional
    public void addRule(UUID id, UUID dynamicRule) {
        statsRepository.addRule(id, dynamicRule);
    }
}
