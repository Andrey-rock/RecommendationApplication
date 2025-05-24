package org.skypro.RecommendationApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Основной класс, запускающий приложение.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@SpringBootApplication
@OpenAPIDefinition
public class RecommendationApplication {

    /**
     * Запуск приложения.
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        SpringApplication.run(RecommendationApplication.class, args);
    }
}
