package org.skypro.RecommendationApplication.repository;

import org.skypro.RecommendationApplication.model.Stats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для сущности Stats.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Repository
public interface StatsRepository extends JpaRepository<Stats, UUID> {

    /**
     * Инкрементирует счетчик срабатывания динамических правил.
     * 
     * @param dynamicRule Динамическое правило.
     */
    @Query(value = "UPDATE stats SET count = count + 1 WHERE rule_id = :dynamicRule;", nativeQuery = true)
    @Modifying
    void incrementCount(UUID dynamicRule);

    /**
     * Добавляет новое динамическое правило в таблицу с начальным значением счетчика "0".
     *
     * @param id Идентификатор правила.
     * @param dynamicRule Динамическое правило.
     */
    @Query(value = "INSERT INTO stats (id, rule_id, count) VALUES (:id, :dynamicRule, 0)", nativeQuery = true)
    @Modifying
    void addRule(UUID id, UUID dynamicRule);
}
