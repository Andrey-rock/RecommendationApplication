package org.skypro.RecommendationApplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.skypro.RecommendationApplication.model.DynamicRule;
import org.skypro.RecommendationApplication.model.Stats;
import org.skypro.RecommendationApplication.service.DynamicRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

/**
 * Контроллер для операций с динамическими правилами.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Tag(name = "Управление продуктами с динамическими правилами")
@RestController
@RequestMapping("/rule")
public class DynamicRuleController {

    private final DynamicRuleService dynamicRuleService;

    public DynamicRuleController(DynamicRuleService dynamicRuleService) {
        this.dynamicRuleService = dynamicRuleService;
    }

    /**
     * Получение информации по всем продуктам
     *
     * @return Список продуктов и динамических правил для них.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить информацию по всем продуктам")
    @GetMapping
    public Collection<DynamicRule> getAllDynamicRules() {
        return dynamicRuleService.getAllDynamicRules();
    }

    /**
     * Добавление продукта с динамическим правилом.
     *
     * @param dynamicRule Динамическое правило.
     * @return Добавленный продукт с динамическим правилом.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Добавить динамическое правило")
    @PostMapping
    public DynamicRule addDynamicRule(@RequestBody DynamicRule dynamicRule) {
        return dynamicRuleService.saveDynamicRule(dynamicRule);
    }


    /**
     * Удаление продукта.
     *
     * @param product_id Идентификатор продукта.
     */
    @DeleteMapping("/{product_id}")
    @Operation(summary = "Удалить динамическое правило")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDynamicRule(@PathVariable UUID product_id) {
        dynamicRuleService.deleteDynamicRuleById(product_id);
    }

    /**
     * Получение статистики срабатыванию динамических правил.
     *
     * @return Статистика.
     */
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить статистику")
    @GetMapping("/stats")
    public Collection<Stats> getStatistics() {
       return dynamicRuleService.getStatistics();
    }
}
