package org.skypro.RecommendationApplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "Dynamic_rule")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicRule {

    @Id
    @GeneratedValue
    private UUID id;

    private String product_name;

    private  UUID product_id;

    private String product_text;

    @JdbcTypeCode(SqlTypes.JSON)
    private Request[] rule;
}
