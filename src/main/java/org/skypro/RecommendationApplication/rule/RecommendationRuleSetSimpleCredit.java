package org.skypro.RecommendationApplication.rule;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationRuleSetSimpleCredit implements RecommendationRuleSet {

    private final Rule rule;

    public RecommendationRuleSetSimpleCredit(Rule rule) {
        this.rule = rule;
    }

    /**
     * @param id
     */
    @Override
    public Optional<RecommendationDTO> getRecommendationByUserId(UUID id) {

        String ID = "59efc529-2fff-41af-baff-90ccd7402925";
        String NAME = "Top Saving";
        String TEXT = """
                Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, \
                который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и \
                потерянных квитанций — всё под контролем!
                Преимущества «Копилки»:
                Накопление средств на конкретные цели. \
                Установите лимит и срок накопления, и банк будет автоматически \
                переводить определенную сумму на ваш счет.
                Прозрачность и контроль. Отслеживайте свои доходы и расходы, \
                контролируйте процесс накопления и корректируйте стратегию при необходимости.
                Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через \
                мобильное приложение или интернет-банкинг.
                Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!""";


        if (rule.checkDebitOperation(id) &&
                ((rule.getAmountDebitDeposits(id) > 50000) || (rule.getAmountSavingDeposits(id) > 50000)) &&
                (rule.getAmountDebitDeposits(id) > rule.getAmountDebitWithdrawals(id))) {
            return Optional.of(new RecommendationDTO(ID, NAME, TEXT));
        } else {
            return Optional.empty();
        }
    }
}
