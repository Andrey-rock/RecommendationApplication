package org.skypro.RecommendationApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Модель составного ключа. Используется для кэширования запросов к БД "transaction" (См. RecommendationsRepository)
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompositeKey {

    private UUID id;
    private String productType;
    private String operationType;
}
