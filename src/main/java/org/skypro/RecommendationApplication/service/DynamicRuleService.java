package org.skypro.RecommendationApplication.service;

import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.skypro.RecommendationApplication.DTO.RecommendationDTO;
import org.skypro.RecommendationApplication.model.DynamicRule;
import org.skypro.RecommendationApplication.model.Request;
import org.skypro.RecommendationApplication.model.Stats;
import org.skypro.RecommendationApplication.repository.DynamicRuleRepository;
import org.skypro.RecommendationApplication.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Сервис управления динамическими правилами.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Service
public class DynamicRuleService {

    private final DynamicRuleRepository dynamicRuleRepository;

    private final RecommendationsRepository recommendationsRepository;

    private final StatsService statsService;

    /**
     * Конструктор класса
     *
     * @param dynamicRuleRepository Репозиторий для управления БД динамических правил
     * @param recommendationsRepository Репозиторий для чтения данных из БД транзакций пользователей
     * @param statsService Сервис сбора статистики срабатывания динамических правил
     */
    public DynamicRuleService(DynamicRuleRepository dynamicRuleRepository, RecommendationsRepository recommendationsRepository, StatsService statsService) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.recommendationsRepository = recommendationsRepository;
        this.statsService = statsService;
    }

    /**
     * Метод получения всех динамических правил рекомендаций, которые есть в базе.
     *
     * @return Динамические правила.
     */
    public Collection<DynamicRule> getAllDynamicRules() {
        return dynamicRuleRepository.findAll();
    }

    /**
     * Метод сохранения нового динамического правила.
     *
     * @param dynamicRule Динамическое правило
     * @return Динамическое правило сохраненное в БД с присвоенным id.
     */
    public DynamicRule saveDynamicRule(DynamicRule dynamicRule) {
        DynamicRule save = dynamicRuleRepository.save(dynamicRule);
        UUID id = UUID.randomUUID();
        statsService.addRule(id, save.getId());
        return save;
    }

    /**
     * Метод удаления динамического правила по идентификатору продукта.
     *
     * @param product_id Идентификатор продукта.
     */
    @Transactional
    public void deleteDynamicRuleById(UUID product_id) {
        dynamicRuleRepository.deleteByProduct_id(product_id);
    }

    /**
     * Метод получения статистики количества срабатываний всех динамических правил.
     *
     * @return Статистика срабатываний динамических правил.
     */
    public Collection<Stats> getStatistics() {
        return statsService.getStats();
    }

    /**
     * Метод выдачи рекомендаций продуктов по динамическим правилам. Если пользователь соответствует
     * всем критериям динамического правила, включает соответствующий продукт в лист рекомендаций.
     * Если пользователь не соответствует ни одному правилу, возвращает пустой лист.
     *
     * @param id Индентификатор пользователя.
     * @return Лист DTO рекомендаций продуктов для пользователя согласно динамическим правилам.
     */
    public List<RecommendationDTO> getDynamicRuleRecommendations(UUID id) {
        List<RecommendationDTO> recommendationDTOs = new ArrayList<>();

        Collection<DynamicRule> allDynamicRules = getAllDynamicRules();
        for (DynamicRule dynamicRule : allDynamicRules) {
            Request[] requests = dynamicRule.getRule();
            boolean flag = true;
            for (Request request : requests) {
                flag = flag && checkRequest(id, request);
            }
            if (flag) {
                RecommendationDTO recommendationDTO = new RecommendationDTO(dynamicRule.getId(),
                        dynamicRule.getProduct_name(),
                        dynamicRule.getProduct_text());
                statsService.incrementCount(dynamicRule.getId());

                recommendationDTOs.add(recommendationDTO);
            }
        }
        return recommendationDTOs;
    }

    /**
     * Метод проверки пользователя на соответствие критериям отдельного запроса из динамического правила. Обрабатывает
     * любые возможные комбинации аргументов запросов динамических правил. При необходимости добавления нового типа
     * запроса, достаточно дописать новый case.
     *
     * @param id Индентификатор пользователя.
     * @param request Запрос к БД. (Динамическое правило содержит несколько запросов).
     * @return true, если пользователь соответствует критериям запроса, иначе false.
     *
     * @throws IllegalStateException, если аргументы запроса некорректны
     */
    private boolean checkRequest(UUID id, @NotNull Request request) {
        String type = request.getQuery();
        String[] arg = request.getArguments();
        boolean neg = request.isNegate();
        boolean result = switch (type) {
            case "USER_OF" -> recommendationsRepository.getCountUsedProductsType(id, arg[0]) > 0;
            case "ACTIVE_USER_OF" -> recommendationsRepository.getCountUsedProductsType(id, arg[0]) > 5;
            case "TRANSACTION_SUM_COMPARE" -> {
                int sum = recommendationsRepository.getSumOperationByProduct(id, arg[0], arg[1]);
                int c = Integer.parseInt(arg[3]);
                boolean b = false;
                switch (arg[2]) {
                    case ">" -> b = sum > c;
                    case "<" -> b = sum < c;
                    case ">=" -> b = sum >= c;
                    case "<=" -> b = sum <= c;
                    case "==" -> b = sum == c;
                }
                yield b;
            }
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> {
                int sumDep = recommendationsRepository.getSumOperationByProduct(id, arg[0], "DEPOSIT");
                int sumWith = recommendationsRepository.getSumOperationByProduct(id, arg[0], "WITHDRAW");
                boolean b = false;
                switch (arg[1]) {
                    case ">" -> b = sumDep > sumWith;
                    case "<" -> b = sumDep < sumWith;
                }
                yield b;
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        if (neg) {
            return !result;
        } else {
            return result;
        }
    }
}
