package org.skypro.RecommendationApplication.service;

import jakarta.transaction.Transactional;
import org.skypro.RecommendationApplication.model.DynamicRule;
import org.skypro.RecommendationApplication.model.Stats;
import org.skypro.RecommendationApplication.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class DynamicRuleService {

    private final DynamicRuleRepository dynamicRuleRepository;

    private final StatsService statsService;

    public DynamicRuleService(DynamicRuleRepository dynamicRuleRepository, StatsService statsService) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.statsService = statsService;
    }

    public Collection<DynamicRule> getAllDynamicRules() {
        return dynamicRuleRepository.findAll();
    }

    public DynamicRule saveDynamicRule(DynamicRule dynamicRule) {
        DynamicRule save = dynamicRuleRepository.save(dynamicRule);
        UUID id = UUID.randomUUID();
        statsService.addRule(id, save.getId());
        return save;
    }

    @Transactional
    public void deleteDynamicRuleById(UUID product_id) {
        dynamicRuleRepository.deleteByProduct_id(product_id);
    }

    public Collection<Stats> getStatistics() {
        return statsService.getStats();
    }
}
