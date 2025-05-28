package org.skypro.RecommendationApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO для выдачи ответа на запрос рекомендаций для пользователя.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private UUID user_id;
    private List<RecommendationDTO> recommendations;
}
