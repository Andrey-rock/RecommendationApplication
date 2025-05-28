package org.skypro.RecommendationApplication.rule;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.service.RuleService;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Реализация RecommendationRuleSet для продукта "Invest 500".
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Component
public class RecommendationRuleSetInvest500 implements RecommendationRuleSet {

    private final static UUID ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
    private final static String NAME = "Invest 500";
    private final static String TEXT = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) " +
            "от нашего банка! " +
            "Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и " +
            "получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить " +
            "свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте " +
            "ближе к финансовой независимости!";

    private final static RecommendationDTO recommendationDTO = new RecommendationDTO(ID, NAME, TEXT);

    private final RuleService ruleService;

    public RecommendationRuleSetInvest500(RuleService ruleService) {
        this.ruleService = ruleService;
    }


    /**
     * Реализация. Проверяет соответствие пользователя следующим правилам:
     * 1. Пользователь использует как минимум один продукт с типом DEBIT.
     * 2. Пользователь не использует продукты с типом INVEST.
     * 3. Сумма пополнений продуктов с типом SAVING больше 1000 ₽.
     *
     * @param id Индентификатор пользователя.
     * @return Рекомендацию для пользвателя, если он подходит, если нет пустой Optional
     */
    @Override
    public Optional<RecommendationDTO> getRecommendationByUserId(UUID id) {

        if (ruleService.checkDebitOperation(id) && (!ruleService.checkInvestOperation(id)) &&
                (ruleService.getAmountSavingDeposits(id) > 1000)) {
            return Optional.of(recommendationDTO);
        } else {
            return Optional.empty();
        }
    }
}
