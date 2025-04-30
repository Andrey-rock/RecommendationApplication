package org.skypro.RecommendationApplication.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getCountUsedProductsType(UUID user, String productType) {
        int result;
        try {
            result = jdbcTemplate.queryForObject(
                    "SELECT COUNT(p.TYPE) количество\n" +
                            "FROM USERS u INNER JOIN TRANSACTIONS t ON u.ID = t.USER_ID\n" +
                            "INNER JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID\n" +
                            "WHERE u.ID = ? AND p.\"TYPE\" = ?\n" +
                            "GROUP BY p.\"TYPE\";",
                    Integer.class,
                    user, productType);
        } catch (DataAccessException e) {
            result = 0;
        }
        return result;
    }

    public int getSumOperationByProduct(UUID user, String productType, String operationType) {
        int result;
        try {
            result = jdbcTemplate.queryForObject(
                    "SELECT SUM(t.AMOUNT) сумма\n" +
                            "FROM USERS u INNER JOIN TRANSACTIONS t ON u.ID = t.USER_ID\n" +
                            "INNER JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID\n" +
                            "WHERE u.ID = ? AND \n" +
                            "p.\"TYPE\" = ? AND t.\"TYPE\" = ? \n" +
                            "GROUP BY t.\"TYPE\";",
                    Integer.class,
                    user, productType, operationType);
        } catch (DataAccessException e) {
            result = 0;
        }
        return result;
    }
}
