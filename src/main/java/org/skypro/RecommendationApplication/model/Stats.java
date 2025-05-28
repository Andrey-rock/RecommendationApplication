package org.skypro.RecommendationApplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Сущность статистики.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Entity
@Table(name = "stats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stats {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")
    @JsonIgnore
    private DynamicRule dynamicRule;

    private int count;
}
