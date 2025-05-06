package org.skypro.RecommendationApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompositeKey {

    private UUID id;
    private String productType;
    private String operationType;
}
