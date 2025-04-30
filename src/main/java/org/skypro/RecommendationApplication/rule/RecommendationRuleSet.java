package org.skypro.RecommendationApplication.rule;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {

    Optional<RecommendationDTO> getRecommendationByUserId(UUID id);
}
