package org.skypro.RecommendationApplication.repository;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.skypro.RecommendationApplication.model.CompositeKey;
import org.skypro.RecommendationApplication.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    private final LoadingCache<CompositeKey, Integer> cache1 = Caffeine.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(k -> this.getCountUsedProductsTypeCash(k.getId(), k.getProductType()));

    private final LoadingCache<CompositeKey, Integer> cache2 = Caffeine.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(k -> this.getSumOperationByProductCash(k.getId(), k.getProductType(), k.getOperationType()));

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getCountUsedProductsType(UUID user, String productType) {
        CompositeKey key = new CompositeKey(user, productType, "");
        return cache1.get(key);
    }

    public int getSumOperationByProduct(UUID user, String productType, String operationType) {
        CompositeKey key = new CompositeKey(user, productType, operationType);
        return cache2.get(key);
    }

    private int getCountUsedProductsTypeCash(UUID user, String productType) {
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

    private int getSumOperationByProductCash(UUID user, String productType, String operationType) {
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

    public User getUserByUsername(String username) {
        final RowMapper<User> userRowMapper = (resultSet, rowNum) -> {
            User user = new User();
            user.setId(UUID.fromString(resultSet.getString("ID")));
            user.setUsername(resultSet.getString("USERNAME"));
            user.setFirst_name(resultSet.getString("FIRST_NAME"));
            user.setLast_name(resultSet.getString("LAST_NAME"));
            return user;
        };
        return jdbcTemplate.queryForObject(
                "SELECT * FROM USERS WHERE USERNAME = ?;",
                userRowMapper,
                username
        );
    }

    public void clearCash() {
        cache1.invalidateAll();
        cache2.invalidateAll();
    }
}
