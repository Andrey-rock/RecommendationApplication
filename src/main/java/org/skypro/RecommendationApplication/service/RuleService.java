package org.skypro.RecommendationApplication.service;

import org.skypro.RecommendationApplication.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RuleService {

    private final RecommendationsRepository recommendationsRepository;

    public RuleService(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    public boolean checkDebitOperation(UUID id) {

        return recommendationsRepository.getCountUsedProductsType(id, "DEBIT") > 0;
    }

    public boolean checkCreditOperation(UUID id) {

        return recommendationsRepository.getCountUsedProductsType(id, "CREDIT") > 0;
    }

    public boolean checkInvestOperation(UUID id) {

        return recommendationsRepository.getCountUsedProductsType(id, "INVEST") > 0;
    }

    public int getAmountDebitDeposits(UUID id) {

        return recommendationsRepository.getSumOperationByProduct(id, "DEBIT",
                "DEPOSIT");
    }

    public int getAmountDebitWithdrawals(UUID id) {

        return recommendationsRepository.getSumOperationByProduct(id, "DEBIT",
                "WITHDRAW");
    }

    public int getAmountSavingDeposits(UUID id) {

        return recommendationsRepository.getSumOperationByProduct(id, "SAVING",
                "DEPOSIT");
    }
}
