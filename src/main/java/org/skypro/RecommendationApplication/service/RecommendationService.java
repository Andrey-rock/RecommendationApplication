package org.skypro.RecommendationApplication.service;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.model.DynamicRule;
import org.skypro.RecommendationApplication.model.Request;
import org.skypro.RecommendationApplication.repository.RecommendationsRepository;
import org.skypro.RecommendationApplication.rule.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationService {


    private final List<RecommendationRuleSet> recommendationRuleSets;

    private final RecommendationsRepository recommendationsRepository;

    private final DynamicRuleService dynamicRuleService;

    private final StatsService statsService;

    public RecommendationService(List<RecommendationRuleSet> recommendationRuleSets, RecommendationsRepository recommendationsRepository, DynamicRuleService dynamicRuleService, StatsService statsService) {
        this.recommendationRuleSets = recommendationRuleSets;
        this.recommendationsRepository = recommendationsRepository;
        this.dynamicRuleService = dynamicRuleService;
        this.statsService = statsService;
    }

    public List<RecommendationDTO> getRecommendations(UUID id) {

        List<RecommendationDTO> recommendationDTOs = new ArrayList<>();
        for (RecommendationRuleSet recommendation : recommendationRuleSets) {
            recommendation.getRecommendationByUserId(id).ifPresent(recommendationDTOs::add);
        }
        List<RecommendationDTO> dynamicRecommendationDTOs = getDynamicRuleRecommendations(id);
        recommendationDTOs.addAll(dynamicRecommendationDTOs);

        return recommendationDTOs;
    }

    private List<RecommendationDTO> getDynamicRuleRecommendations(UUID id) {
        List<RecommendationDTO> recommendationDTOs = new ArrayList<>();

        Collection<DynamicRule> allDynamicRules = dynamicRuleService.getAllDynamicRules();
        for (DynamicRule dynamicRule : allDynamicRules) {
            Request[] requests = dynamicRule.getRule();
            boolean flag = true;
            for (Request request : requests) {
                flag = flag && checkRequest(id, request);
            }
            if (flag) {
                RecommendationDTO recommendationDTO = new RecommendationDTO(dynamicRule.getId(),
                        dynamicRule.getProduct_name(),
                        dynamicRule.getProduct_text());
                statsService.incrementCount(dynamicRule.getId());

                recommendationDTOs.add(recommendationDTO);
            }
        }
        return recommendationDTOs;
    }

    private boolean checkRequest(UUID id, Request request) {
        String type = request.getQuery();
        String[] arg = request.getArguments();
        boolean neg = request.isNegate();
        boolean result = switch (type) {
            case "USER_OF" -> recommendationsRepository.getCountUsedProductsType(id, arg[0]) > 0;
            case "ACTIVE_USER_OF" -> recommendationsRepository.getCountUsedProductsType(id, arg[0]) > 5;
            case "TRANSACTION_SUM_COMPARE" -> {
                int sum = recommendationsRepository.getSumOperationByProduct(id, arg[0], arg[1]);
                int c = Integer.parseInt(arg[3]);
                boolean b = false;
                switch (arg[2]) {
                    case ">" -> b = sum > c;
                    case "<" -> b = sum < c;
                    case ">=" -> b = sum >= c;
                    case "<=" -> b = sum <= c;
                    case "==" -> b = sum == c;
                }
                yield b;
            }
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> {
                int sumDep = recommendationsRepository.getSumOperationByProduct(id, arg[0], "DEPOSIT");
                int sumWith = recommendationsRepository.getSumOperationByProduct(id, arg[0], "WITHDRAW");
                boolean b = false;
                switch (arg[1]) {
                    case ">" -> b = sumDep > sumWith;
                    case "<" -> b = sumDep < sumWith;
                }
                yield b;
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        if (neg) {
            return !result;
        }
        else {
            return result;
        }
    }
}
