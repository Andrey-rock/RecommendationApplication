package org.skypro.RecommendationApplication.repository;

import org.apache.tomcat.util.digester.Rule;
import org.skypro.RecommendationApplication.model.Stats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatsRepository extends JpaRepository<Stats, UUID> {

    @Query(value = "UPDATE stats SET count = count + 1 WHERE rule_id = :dynamicRule;", nativeQuery = true)
    @Modifying
    void incrementCount(UUID dynamicRule);

    @Query(value = "INSERT INTO stats (id, rule_id, count) VALUES (:id, :dynamicRule, 0)", nativeQuery = true)
    @Modifying
    void addRule(UUID id, UUID dynamicRule);
}
