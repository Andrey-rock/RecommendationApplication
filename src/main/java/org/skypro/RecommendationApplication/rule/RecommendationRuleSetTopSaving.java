package org.skypro.RecommendationApplication.rule;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationRuleSetTopSaving implements RecommendationRuleSet {

    private final Rule rule;

    public RecommendationRuleSetTopSaving(Rule rule) {
        this.rule = rule;
    }

    /**
     * @param id
     */
    @Override
    public Optional<RecommendationDTO> getRecommendationByUserId(UUID id) {

        String ID = "ab138afb-f3ba-4a93-b74f-0fcee86d447f";
        String NAME = "Простой кредит";
        String TEXT = """
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

        if (!rule.checkCreditOperation(id) &&
                (rule.getAmountDebitDeposits(id) > 100000) &&
                (rule.getAmountDebitDeposits(id) > rule.getAmountDebitWithdrawals(id))) {
            return Optional.of(new RecommendationDTO(ID, NAME, TEXT));
        } else {
            return Optional.empty();
        }
    }
}
