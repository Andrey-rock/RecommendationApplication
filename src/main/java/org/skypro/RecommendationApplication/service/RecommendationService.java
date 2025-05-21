package org.skypro.RecommendationApplication.service;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.rule.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> recommendationRuleSets;

    private final DynamicRuleService dynamicRuleService;

    public RecommendationService(List<RecommendationRuleSet> recommendationRuleSets, DynamicRuleService dynamicRuleService) {
        this.recommendationRuleSets = recommendationRuleSets;
        this.dynamicRuleService = dynamicRuleService;
    }

    public List<RecommendationDTO> getRecommendations(UUID id) {

        List<RecommendationDTO> recommendationDTOs = new ArrayList<>();
        for (RecommendationRuleSet recommendation : recommendationRuleSets) {
            recommendation.getRecommendationByUserId(id).ifPresent(recommendationDTOs::add);
        }
        List<RecommendationDTO> dynamicRecommendationDTOs = dynamicRuleService.getDynamicRuleRecommendations(id);
        recommendationDTOs.addAll(dynamicRecommendationDTOs);

        return recommendationDTOs;
    }
}
