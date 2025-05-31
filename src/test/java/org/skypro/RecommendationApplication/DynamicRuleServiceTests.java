package org.skypro.RecommendationApplication;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.model.DynamicRule;
import org.skypro.RecommendationApplication.model.Request;
import org.skypro.RecommendationApplication.repository.DynamicRuleRepository;
import org.skypro.RecommendationApplication.repository.RecommendationsRepository;
import org.skypro.RecommendationApplication.service.DynamicRuleService;
import org.skypro.RecommendationApplication.service.StatsService;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DynamicRuleServiceTests {

    @Mock
    private DynamicRuleRepository dynamicRuleRepository;

    @Mock
    private RecommendationsRepository recommendationsRepository;

    @Mock
    private StatsService statsService;

    @InjectMocks
    private DynamicRuleService dynamicRuleService;

    //Положительный тест-кейс. Пользователь получает рекомендацию. В статистике увеличивается счетчик.
    @Test
    public void whenDynamicRuleAppropriate_thenRecommendProduct() {
        UUID userId = UUID.randomUUID();
        DynamicRule dynamicRule = createDynamicRule();
        Mockito.when(dynamicRuleRepository.findAll()).thenReturn(List.of(dynamicRule));
        Mockito.when(recommendationsRepository.getCountUsedProductsType(userId, "INVEST")).thenReturn(2);
        Mockito.when(recommendationsRepository.getCountUsedProductsType(userId, "CREDIT")).thenReturn(6);
        Mockito.when(recommendationsRepository.getSumOperationByProduct(userId, "DEBIT", "DEPOSIT"))
                .thenReturn(10000);
        Mockito.when(recommendationsRepository.getSumOperationByProduct(userId, "DEBIT", "WITHDRAW"))
                .thenReturn(5000);

        List<RecommendationDTO> result = dynamicRuleService.getDynamicRuleRecommendations(userId);
        UUID id = dynamicRule.getId();
        String productName = dynamicRule.getProduct_name();
        String productText = dynamicRule.getProduct_text();

        Assertions.assertEquals(id, result.get(0).getId());
        Assertions.assertEquals(productName, result.get(0).getName());
        Assertions.assertEquals(productText, result.get(0).getText());
        Mockito.verify(statsService).incrementCount(id);

    }

    //Отрицательный тест-кейс
    @Test
    public void whenDynamicRuleNotAppropriate_thenReturnEmptyList() {
        UUID userId = UUID.randomUUID();
        DynamicRule dynamicRule = createDynamicRule();
        Mockito.when(dynamicRuleRepository.findAll()).thenReturn(List.of(dynamicRule));
        Mockito.when(recommendationsRepository.getCountUsedProductsType(userId, "INVEST")).thenReturn(0, 2, 6, 7);
        Mockito.when(recommendationsRepository.getCountUsedProductsType(userId, "CREDIT")).thenReturn(8, 1, 6, 7);
        Mockito.when(recommendationsRepository.getSumOperationByProduct(userId, "DEBIT", "DEPOSIT"))
                .thenReturn(10000, 10000, 500, 5000);
        Mockito.when(recommendationsRepository.getSumOperationByProduct(userId, "DEBIT", "WITHDRAW"))
                .thenReturn(6000, 5000, 100, 6000);

        List<RecommendationDTO> result = dynamicRuleService.getDynamicRuleRecommendations(userId);

        Assertions.assertEquals(List.of(), result);
    }

    // Метод создания динамического правила для тестов
    @NotNull
    private DynamicRule createDynamicRule() {
        Request request1 = new Request("USER_OF", new String[]{"INVEST"}, false);
        Request request2 = new Request("ACTIVE_USER_OF", new String[]{"CREDIT"}, false);
        Request request3 = new Request("TRANSACTION_SUM_COMPARE", new String[]{"DEBIT", "DEPOSIT", ">", "1000"},
                false);
        Request request4 = new Request("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW",
                new String[]{"DEBIT", ">"}, false);
        Request[] requests = new Request[]{request1, request2, request3, request4};

        DynamicRule dynamicRule = new DynamicRule();
        dynamicRule.setId(UUID.randomUUID());
        dynamicRule.setProduct_id(UUID.randomUUID());
        dynamicRule.setProduct_name("Test");
        dynamicRule.setProduct_text("Test text");
        dynamicRule.setRule(requests);
        return dynamicRule;
    }
}
