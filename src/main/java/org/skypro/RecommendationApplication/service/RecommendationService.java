package org.skypro.RecommendationApplication.service;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.rule.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> recommendationRuleSets;

    public RecommendationService(List<RecommendationRuleSet> recommendationRuleSets) {
        this.recommendationRuleSets = recommendationRuleSets;
    }

    public List<RecommendationDTO> getRecommendations(UUID id) {
        List<RecommendationDTO> recommendationDTOs = new ArrayList<>();
        for (RecommendationRuleSet ruleSet : recommendationRuleSets) {
            ruleSet.getRecommendationByUserId(id).ifPresent(recommendationDTOs::add);
        }
        return recommendationDTOs;
    }
}
