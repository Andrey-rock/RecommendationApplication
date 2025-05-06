package org.skypro.RecommendationApplication.repository;

import org.skypro.RecommendationApplication.model.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID> {

    @Query(value = "DELETE from public.dynamic_rule where product_id = :product_id", nativeQuery = true)
    @Modifying
    void deleteByProduct_id(UUID product_id);
}
