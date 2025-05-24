package org.skypro.RecommendationApplication.rule;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;

import java.util.Optional;
import java.util.UUID;

/**
 * Общий интерфейс статичных правил.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
public interface RecommendationRuleSet {

    Optional<RecommendationDTO> getRecommendationByUserId(UUID id);
}
