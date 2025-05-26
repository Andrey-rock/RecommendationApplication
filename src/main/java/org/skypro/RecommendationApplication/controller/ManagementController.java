package org.skypro.RecommendationApplication.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.skypro.RecommendationApplication.DTO.Info;
import org.skypro.RecommendationApplication.repository.RecommendationsRepository;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * Сервисный контроллер для менеджеров для сброса кэша и получения информации о приложении.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Tag(name = "Для менеджеров")
@RestController
@RequestMapping("/management")
public class ManagementController {

    private final BuildProperties properties;

    private final RecommendationsRepository recommendationsRepository;

    public ManagementController(BuildProperties properties, RecommendationsRepository recommendationsRepository) {
        this.properties = properties;
        this.recommendationsRepository = recommendationsRepository;
    }

    /**
     * Сброс кэша
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Сбросить кэш")
    @PostMapping("/clear-caches")
    public void clearCaches() {
        recommendationsRepository.clearCash();
    }

    /**
     * Получение информации о приложении.
     *
     * @return Информация о приложении.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить информацию о приложении")
    @GetMapping("/info")
    public Info info() {
        return new Info(properties.getName(), properties.getVersion());
    }
}
