package org.skypro.RecommendationApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private UUID user_id;
    private List<RecommendationDTO> recommendations;
}
