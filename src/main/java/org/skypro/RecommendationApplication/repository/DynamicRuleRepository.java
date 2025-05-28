package org.skypro.RecommendationApplication.repository;

import org.skypro.RecommendationApplication.model.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Репозиторий для сущности DynamicRule.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID> {

    /**
     * Удаляет динамическое правило по идентификатору продукта.
     *
     * @param product_id Идентификатор продукта.
     */
    @Query(value = "DELETE from public.dynamic_rule where product_id = :product_id", nativeQuery = true)
    @Modifying
    void deleteByProduct_id(UUID product_id);
}
