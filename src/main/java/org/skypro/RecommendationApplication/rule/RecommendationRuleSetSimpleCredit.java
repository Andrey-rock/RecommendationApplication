package org.skypro.RecommendationApplication.rule;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.service.RuleService;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationRuleSetSimpleCredit implements RecommendationRuleSet {

    private static final String ID = "ab138afb-f3ba-4a93-b74f-0fcee86d447f";
    private static final String NAME = "Простой кредит";
    private static final String TEXT = """
                Откройте мир выгодных кредитов с нами!
                Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то,
                что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.
                Почему выбирают нас:
                Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего
                несколько часов.
                Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.
                Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости,
                автомобиля, образование, лечение и многое другое.
                Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!""";

    private final static RecommendationDTO recommendationDTO = new RecommendationDTO(ID, NAME, TEXT);

    private final RuleService ruleService;

    public RecommendationRuleSetSimpleCredit(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    /**
     * @param id
     */
    @Override
    public Optional<RecommendationDTO> getRecommendationByUserId(UUID id) {

        if (!ruleService.checkCreditOperation(id) &&
                (ruleService.getAmountDebitDeposits(id) > 100000) &&
                (ruleService.getAmountDebitDeposits(id) > ruleService.getAmountDebitWithdrawals(id))) {
            return Optional.of(recommendationDTO);
        } else {
            return Optional.empty();
        }
    }
}
