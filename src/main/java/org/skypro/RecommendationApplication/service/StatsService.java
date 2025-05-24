package org.skypro.RecommendationApplication.service;

import jakarta.transaction.Transactional;

import org.skypro.RecommendationApplication.model.Stats;
import org.skypro.RecommendationApplication.repository.StatsRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

/**
 * Сервис сбора статистики срабатывания динамических правил.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Service
public class StatsService {

    private final StatsRepository statsRepository;

    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    /**
     * Метод получения статистики.
     *
     * @return Статистика выдачи рекомендаций.
     */
    public Collection<Stats> getStats() {
        return statsRepository.findAll();
    }

    /**
     * Метод увиличения счетчика выдачи рекомендации по конкретному динамическому правилу на 1.
     *
     * @param dynamicRule Динамическое правило для которого нужно увеличить счетчик.
     */
    @Transactional
    public void incrementCount(UUID dynamicRule) {
        statsRepository.incrementCount(dynamicRule);
    }

    /**
     * Метод добавления динамического правила в таблицу статистики.
     *
     * @param id          Идентификатор динамического правила в таблице статистики
     *                    (Генерируется случайним образом в методе saveDynamicRule сервиса DynamicRuleService).
     * @param dynamicRule Динамическое правило, которое нужно добавить в список статистики.
     */
    @Transactional
    public void addRule(UUID id, UUID dynamicRule) {
        statsRepository.addRule(id, dynamicRule);
    }
}
