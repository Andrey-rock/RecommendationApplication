package org.skypro.RecommendationApplication.rule;

import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationRuleSetInvest500 implements RecommendationRuleSet {

    private final Rule rule;

    public RecommendationRuleSetInvest500(Rule rule) {
        this.rule = rule;
    }

    /**
     * @param id
     */
    @Override
    public Optional<RecommendationDTO> getRecommendationByUserId(UUID id) {

        String ID = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
        String NAME = "Invest 500";
        String TEXT = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! " +
                "Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и " +
                "получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить " +
                "свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте " +
                "ближе к финансовой независимости!";

        if (rule.checkDebitOperation(id) && (!rule.checkInvestOperation(id)) && (rule.getAmountSavingDeposits(id) > 1000)) {
            return Optional.of(new RecommendationDTO(ID, NAME, TEXT));
        } else {
            return Optional.empty();
        }
    }
}
