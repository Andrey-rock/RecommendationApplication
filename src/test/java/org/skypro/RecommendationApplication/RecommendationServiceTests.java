package org.skypro.RecommendationApplication;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.rule.*;
import org.skypro.RecommendationApplication.service.RecommendationService;
import org.skypro.RecommendationApplication.service.RuleService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTests {

    @Mock
    private RuleService ruleService;

    @InjectMocks
    private RecommendationRuleSetInvest500 recommendationRuleSetInvest500;

    @InjectMocks
    private RecommendationRuleSetTopSaving recommendationRuleSetTopSaving;

    @InjectMocks
    private RecommendationRuleSetSimpleCredit recommendationRuleSetSimpleCredit;

    private RecommendationService recommendationService;

    @BeforeEach
    public void setUp() {
        List<RecommendationRuleSet> recommendationRuleSets = new ArrayList<>();
        recommendationRuleSets.add(recommendationRuleSetInvest500);
        recommendationRuleSets.add(recommendationRuleSetTopSaving);
        recommendationRuleSets.add(recommendationRuleSetSimpleCredit);
        recommendationService = new RecommendationService(recommendationRuleSets);
    }

    //Тест на выдачу рекомендации продукта "Invest500"
    @Test
    void whenFirstRecommendationAppropriate_thenRecommendInvest500() {
        UUID productId = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
        String productName = "Invest 500";
        UUID id = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        Mockito.when(ruleService.checkDebitOperation(id)).thenReturn(true);
        Mockito.when(ruleService.checkInvestOperation(id)).thenReturn(false);
        Mockito.when(ruleService.checkCreditOperation(id)).thenReturn(true);
        Mockito.when(ruleService.getAmountSavingDeposits(id)).thenReturn(1001);
        Mockito.when(ruleService.getAmountDebitDeposits(id)).thenReturn(1000);

        List<RecommendationDTO> recommendations = recommendationService.getRecommendations(id);

        Assertions.assertEquals(productId, recommendations.get(0).getId());
        Assertions.assertEquals(productName, recommendations.get(0).getName());
    }

    //Тест на выдачу рекомендации продукта "Invest500", если Сумма пополнений по всем продуктам типа DEBIT >= 50 000 ₽
    @Test
    void whenSecondRecommendationAppropriateDebit_thenRecommendTopSaving() {
        UUID productId = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
        String productName = "Top Saving";
        UUID id = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
        Mockito.when(ruleService.checkDebitOperation(id)).thenReturn(true);
        Mockito.when(ruleService.checkInvestOperation(id)).thenReturn(true);
        Mockito.when(ruleService.checkCreditOperation(id)).thenReturn(true);
        Mockito.when(ruleService.getAmountDebitDeposits(id)).thenReturn(50001);
        Mockito.when(ruleService.getAmountDebitWithdrawals(id)).thenReturn(1000);

        List<RecommendationDTO> recommendations = recommendationService.getRecommendations(id);

        Assertions.assertEquals(productId, recommendations.get(0).getId());
        Assertions.assertEquals(productName, recommendations.get(0).getName());
    }

    //Тест на выдачу рекомендации продукта "Invest500", если Сумма пополнений по всем продуктам типа SAVING >= 50 000 ₽
    @Test
    void whenSecondRecommendationAppropriateSaving_thenRecommendTopSaving() {
        UUID productId = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
        String productName = "Top Saving";
        UUID id = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
        Mockito.when(ruleService.checkDebitOperation(id)).thenReturn(true);
        Mockito.when(ruleService.checkInvestOperation(id)).thenReturn(true);
        Mockito.when(ruleService.checkCreditOperation(id)).thenReturn(true);
        Mockito.when(ruleService.getAmountDebitDeposits(id)).thenReturn(2000);
        Mockito.when(ruleService.getAmountSavingDeposits(id)).thenReturn(50001);
        Mockito.when(ruleService.getAmountDebitWithdrawals(id)).thenReturn(1000);

        List<RecommendationDTO> recommendations = recommendationService.getRecommendations(id);

        Assertions.assertEquals(productId, recommendations.get(0).getId());
        Assertions.assertEquals(productName, recommendations.get(0).getName());
    }

    //Тест на выдачу рекомендации продукта "Простой кредит"
    @Test
    void whenThirdRecommendationAppropriate_thenRecommendSimpleCredit() {
        UUID productId = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");
        String productName = "Простой кредит";
        UUID id = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");
        Mockito.when(ruleService.checkDebitOperation(id)).thenReturn(false);
        Mockito.when(ruleService.checkCreditOperation(id)).thenReturn(false);
        Mockito.when(ruleService.getAmountDebitDeposits(id)).thenReturn(101000);
        Mockito.when(ruleService.getAmountDebitWithdrawals(id)).thenReturn(10000);

        List<RecommendationDTO> recommendations = recommendationService.getRecommendations(id);

        Assertions.assertEquals(productId, recommendations.get(0).getId());
        Assertions.assertEquals(productName, recommendations.get(0).getName());
    }

    //Тест на выдачу пустой коллекции, если не подходит ни одна рекомендация"
    @Test
    void whenNoRecommendations_thenReturnEmptyList() {

        UUID id = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");
        Mockito.when(ruleService.checkDebitOperation(id)).thenReturn(false);
        Mockito.when(ruleService.checkCreditOperation(id)).thenReturn(true);

        List<RecommendationDTO> recommendations = recommendationService.getRecommendations(id);

        Assertions.assertEquals(List.of(), recommendations);
    }
}
