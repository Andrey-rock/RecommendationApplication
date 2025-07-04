package org.skypro.RecommendationApplication.rule;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.service.RuleService;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Реализация RecommendationRuleSet для продукта "Top Saving".
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Component
public class RecommendationRuleSetTopSaving implements RecommendationRuleSet {

    private static final UUID ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private static final String NAME = "Top Saving";
    private static final String TEXT = """
                Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент,
                который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и
                потерянных квитанций — всё под контролем!
                Преимущества «Копилки»:
                Накопление средств на конкретные цели.
                Установите лимит и срок накопления, и банк будет автоматически
                переводить определенную сумму на ваш счет.
                Прозрачность и контроль. Отслеживайте свои доходы и расходы,
                контролируйте процесс накопления и корректируйте стратегию при необходимости.
                Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через
                мобильное приложение или интернет-банкинг.
                Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!""";

    private final static RecommendationDTO recommendationDTO = new RecommendationDTO(ID, NAME, TEXT);

    private final RuleService ruleService;

    public RecommendationRuleSetTopSaving(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    /**
     * Реализация. Проверяет соответствие пользователя следующим правилам:
     * 1. Пользователь использует как минимум один продукт с типом DEBIT.
     * 2. Сумма пополнений по всем продуктам типа DEBIT больше или равна 50 000 ₽ ИЛИ
     * Сумма пополнений по всем продуктам типа SAVING больше или равна 50 000 ₽.
     * 3. Сумма пополнений по всем продуктам типа DEBIT больше, чем сумма трат по всем продуктам типа DEBIT.
     *
     * @param id Индентификатор пользователя.
     * @return Рекомендацию для пользвателя, если он подходит, если нет пустой Optional
     */
    @Override
    public Optional<RecommendationDTO> getRecommendationByUserId(UUID id) {

        if (ruleService.checkDebitOperation(id) &&
                ((ruleService.getAmountDebitDeposits(id) > 50000) || (ruleService.getAmountSavingDeposits(id) > 50000)) &&
                (ruleService.getAmountDebitDeposits(id) > ruleService.getAmountDebitWithdrawals(id))) {
            return Optional.of(recommendationDTO);
        } else {
            return Optional.empty();
        }
    }
}
