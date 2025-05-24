package org.skypro.RecommendationApplication.service;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.rule.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервис выдачи рекомендаций новых продуктов для пользователя.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> recommendationRuleSets;

    private final DynamicRuleService dynamicRuleService;

    /**
     * Конструктор класса
     *
     * @param recommendationRuleSets Коллекция статических правил выдачи рекомендаций описываемых общим интерфейсом.
     * @param dynamicRuleService     Сервис управления динамическими правилами.
     */
    public RecommendationService(List<RecommendationRuleSet> recommendationRuleSets, DynamicRuleService dynamicRuleService) {
        this.recommendationRuleSets = recommendationRuleSets;
        this.dynamicRuleService = dynamicRuleService;
    }

    /**
     * Метод выдачи рекомендаций пользователю.
     *
     * @param id Индентификатор пользователя.
     * @return Рекомендуемые продукты для пользователя.
     */
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
