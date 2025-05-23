package org.skypro.RecommendationApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationDTO {
    private UUID id;
    private String name;
    private String text;
}
