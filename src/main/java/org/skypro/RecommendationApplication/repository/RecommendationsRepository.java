package org.skypro.RecommendationApplication.repository;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.skypro.RecommendationApplication.exeption.UserNotFoundException;
import org.skypro.RecommendationApplication.model.CompositeKey;
import org.skypro.RecommendationApplication.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Репозиторий для работы с БД "transaction".
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    //Кэш для результатов запроса метода getCountUsedProductsTypeCash
    private final LoadingCache<CompositeKey, Integer> cache1 = Caffeine.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(k -> this.getCountUsedProductsTypeCash(k.getId(), k.getProductType()));

    //Кэш для результатов запроса метода getSumOperationByProductCash
    private final LoadingCache<CompositeKey, Integer> cache2 = Caffeine.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(k -> this.getSumOperationByProductCash(k.getId(), k.getProductType(), k.getOperationType()));

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Возвращает количество операций пользователя с продуктом из кэша. Если в кэше ещё нет нужного результата
     * возвращает результат метода getCountUsedProductsTypeCash и кладет его в кэш.
     *
     * @param user Индентификатор пользователя.
     * @param productType Тип продукта (DEBIT, CREDIT, SAVING или INVEST)
     * @return Количество операций с продуктом.
     */
    public int getCountUsedProductsType(UUID user, String productType) {
        CompositeKey key = new CompositeKey(user, productType, "");
        return cache1.get(key);
    }

    /**
     * Возвращает итоговую сумму операций пользователя с продуктом из кэша. Если в кэше ещё нет нужного результата
     * возвращает результат метода getSumOperationByProductCash и кладет его в кэш.
     *
     * @param user Индентификатор пользователя.
     * @param productType Тип продукта (DEBIT, CREDIT, SAVING или INVEST).
     * @param operationType Тип операции (DEPOSIT или WITHDRAW).
     * @return Итоговую сумму операций.
     */
    public int getSumOperationByProduct(UUID user, String productType, String operationType) {
        CompositeKey key = new CompositeKey(user, productType, operationType);
        return cache2.get(key);
    }

    /**
     * Возвращает количество операций пользователя с продуктом из БД.
     * Если операций нет queryForObject кидает исключение DataAccessException, в этом случае возвращается 0.
     *
     * @param user Индентификатор пользователя.
     * @param productType Тип продукта (DEBIT, CREDIT, SAVING или INVEST)
     * @return Количество операций с продуктом.
     */
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

    /**
     * Возвращает итоговую сумму операций пользователя с продуктом из БД.
     * Если операций нет queryForObject кидает исключение DataAccessException, в этом случае возвращается 0.
     *
     * @param user Идентификатор пользователя.
     * @param productType Тип продукта (DEBIT, CREDIT, SAVING или INVEST).
     * @param operationType Тип операции (DEPOSIT или WITHDRAW).
     * @return Итоговую сумму операций.
     */
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

    /**
     * Возвращает пользователя по нику.
     *
     * @param username Ник пользователя.
     * @return Объект пользователя.
     * @throws UserNotFoundException, если пользователь с заданным ником не найден в БД.
     */
    public User getUserByUsername(String username) throws UserNotFoundException {
        final RowMapper<User> userRowMapper = (resultSet, rowNum) -> {
            User user = new User();
            user.setId(UUID.fromString(resultSet.getString("ID")));
            user.setUsername(resultSet.getString("USERNAME"));
            user.setFirst_name(resultSet.getString("FIRST_NAME"));
            user.setLast_name(resultSet.getString("LAST_NAME"));
            return user;
        };
        User user;
        try {
            user = jdbcTemplate.queryForObject(
                    "SELECT * FROM USERS WHERE USERNAME = ?;",
                    userRowMapper,
                    username
            );
        } catch (DataAccessException e) {
            throw new UserNotFoundException();
        }
        return user;
    }

    /**
     * Сброс кэша.
     */
    public void clearCash() {
        cache1.invalidateAll();
        cache2.invalidateAll();
    }
}
