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

    /**
     * @param id Индентификатор пользователя.
     * @return Рекомендацию для пользвателя, если он подходит, если нет пустой Optional
     */
    Optional<RecommendationDTO> getRecommendationByUserId(UUID id);
}
