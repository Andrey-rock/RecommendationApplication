package org.skypro.RecommendationApplication.controller;

import org.skypro.RecommendationApplication.model.DynamicRule;
import org.skypro.RecommendationApplication.service.DynamicRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
public class DynamicRuleController {

    private final DynamicRuleService dynamicRuleService;

    public DynamicRuleController(DynamicRuleService dynamicRuleService) {
        this.dynamicRuleService = dynamicRuleService;
    }

    @GetMapping("/rule")
    public Collection<DynamicRule> getAllDynamicRules() {
        return dynamicRuleService.getAllDynamicRules();
    }

    @PostMapping("/rule")
    public DynamicRule addDynamicRule(@RequestBody DynamicRule dynamicRule) {
        return dynamicRuleService.saveDynamicRule(dynamicRule);
    }

    @DeleteMapping("/rule/{product_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDynamicRule(@PathVariable UUID product_id) {
        dynamicRuleService.deleteDynamicRuleById(product_id);
    }
}
