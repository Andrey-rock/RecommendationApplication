package org.skypro.RecommendationApplication.service;

import jakarta.transaction.Transactional;
import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.model.DynamicRule;
import org.skypro.RecommendationApplication.model.Request;
import org.skypro.RecommendationApplication.model.Stats;
import org.skypro.RecommendationApplication.repository.DynamicRuleRepository;
import org.skypro.RecommendationApplication.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class DynamicRuleService {

    private final DynamicRuleRepository dynamicRuleRepository;

    private final RecommendationsRepository recommendationsRepository;

    private final StatsService statsService;

    public DynamicRuleService(DynamicRuleRepository dynamicRuleRepository, RecommendationsRepository recommendationsRepository, StatsService statsService) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.recommendationsRepository = recommendationsRepository;
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

    public List<RecommendationDTO> getDynamicRuleRecommendations(UUID id) {
        List<RecommendationDTO> recommendationDTOs = new ArrayList<>();

        Collection<DynamicRule> allDynamicRules = getAllDynamicRules();
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
        } else {
            return result;
        }
    }
}
