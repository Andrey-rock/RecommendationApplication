package org.skypro.RecommendationApplication.rule;

import org.skypro.RecommendationApplication.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Rule {

    private final RecommendationsRepository recommendationsRepository;

    public Rule(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    protected boolean checkDebitOperation(UUID id) {

        return recommendationsRepository.getCountUsedProductsType(id, "DEBIT") > 0;
    }

    protected boolean checkCreditOperation(UUID id) {

        return recommendationsRepository.getCountUsedProductsType(id, "CREDIT") > 0;
    }

    protected boolean checkInvestOperation(UUID id) {

        return recommendationsRepository.getCountUsedProductsType(id, "INVEST") > 0;
    }

    protected int getAmountDebitDeposits(UUID id) {
        return recommendationsRepository.getSumOperationByProduct(id, "DEBIT",
                "DEPOSIT");
    }

    protected int getAmountDebitWithdrawals(UUID id) {
        return recommendationsRepository.getSumOperationByProduct(id, "DEBIT",
                "WITHDRAW");
    }

    protected int getAmountSavingDeposits(UUID id) {

        return recommendationsRepository.getSumOperationByProduct(id, "SAVING",
                "DEPOSIT");
    }
}
