package org.skypro.RecommendationApplication.service;

import org.skypro.RecommendationApplication.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Сервис проверки выполнения правил для пользователя.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Service
public class RuleService {

    private final RecommendationsRepository recommendationsRepository;

    public RuleService(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    /**
     * Метод проверки использует ли пользователь продукты с типом DEBIT.
     *
     * @param id Индентификатор пользователя.
     * @return true, если пользователь использует продукты с типом DEBIT, иначе false.
     */
    public boolean checkDebitOperation(UUID id) {

        return recommendationsRepository.getCountUsedProductsType(id, "DEBIT") > 0;
    }

    /**
     * Метод проверки использует ли пользователь продукты с типом CREDIT.
     *
     * @param id Индентификатор пользователя.
     * @return true, если пользователь использует продукты с типом CREDIT, иначе false.
     */
    public boolean checkCreditOperation(UUID id) {

        return recommendationsRepository.getCountUsedProductsType(id, "CREDIT") > 0;
    }

    /**
     * Метод проверки использует ли пользователь продукты с типом INVEST.
     *
     * @param id Индентификатор пользователя.
     * @return true, если пользователь использует продукты с типом INVEST, иначе false.
     */
    public boolean checkInvestOperation(UUID id) {

        return recommendationsRepository.getCountUsedProductsType(id, "INVEST") > 0;
    }

    /**
     * Метод получения суммы пополнений по продукту типа DEBIT.
     *
     * @param id Индентификатор пользователя.
     * @return Сумма пополнений по продукту типа DEBIT.
     */
    public int getAmountDebitDeposits(UUID id) {

        return recommendationsRepository.getSumOperationByProduct(id, "DEBIT",
                "DEPOSIT");
    }

    /**
     * Метод получения суммы списаний по продукту типа DEBIT.
     *
     * @param id Индентификатор пользователя.
     * @return Сумма списаний по продукту типа DEBIT.
     */
    public int getAmountDebitWithdrawals(UUID id) {

        return recommendationsRepository.getSumOperationByProduct(id, "DEBIT",
                "WITHDRAW");
    }

    /**
     * Метод получения суммы пополнений по продукту типа SAVING.
     *
     * @param id Индентификатор пользователя.
     * @return Сумма пополнений по продукту типа SAVING.
     */
    public int getAmountSavingDeposits(UUID id) {

        return recommendationsRepository.getSumOperationByProduct(id, "SAVING",
                "DEPOSIT");
    }
}
