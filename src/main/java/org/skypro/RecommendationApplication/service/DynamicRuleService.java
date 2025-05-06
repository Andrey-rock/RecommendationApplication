package org.skypro.RecommendationApplication.service;

import jakarta.transaction.Transactional;
import org.skypro.RecommendationApplication.model.DynamicRule;
import org.skypro.RecommendationApplication.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class DynamicRuleService {

    private final DynamicRuleRepository dynamicRuleRepository;

    public DynamicRuleService(DynamicRuleRepository dynamicRuleRepository) {
        this.dynamicRuleRepository = dynamicRuleRepository;
    }

    public Collection<DynamicRule> getAllDynamicRules() {
        return dynamicRuleRepository.findAll();
    }

    public DynamicRule saveDynamicRule(DynamicRule dynamicRule) {
        return dynamicRuleRepository.save(dynamicRule);
    }

    @Transactional
    public void deleteDynamicRuleById(UUID product_id) {
        dynamicRuleRepository.deleteByProduct_id(product_id);
    }
}
