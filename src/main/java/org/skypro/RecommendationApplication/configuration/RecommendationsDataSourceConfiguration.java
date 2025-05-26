package org.skypro.RecommendationApplication.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.DeleteMyCommands;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Класс конфигурации.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Configuration
public class RecommendationsDataSourceConfiguration {

    /**
     * Конфигурирование источника данных для БД продуктов с динамическими правилами.
     *
     * @param properties параметры БД.
     * @return Основной источник данных.
     */
    @Primary
    @Bean(name = "defaultDataSource")
    public DataSource defaultDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    /**
     * Конфигурирование источника данных для БД транзакций.
     *
     * @param recommendationsUrl Путь к файлу БД.
     * @return Источник данных.
     */
    @Bean(name = "recommendationsDataSource")
    public DataSource recommendationsDataSource(@Value("${application.recommendations-db.url}") String recommendationsUrl) {
        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(recommendationsUrl);
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setReadOnly(true);
        return dataSource;
    }

    /**
     * Создание объекта dbcTemplate.
     *
     * @param dataSource Источник данных.
     * @return JdbcTemplate.
     */
    @Bean(name = "recommendationsJdbcTemplate")
    public JdbcTemplate recommendationsJdbcTemplate(
            @Qualifier("recommendationsDataSource") DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }

    @Value("${telegram.bot.token}")
    private String token;

    /**
     * Конфигурирование телеграм-бота.
     *
     * @return Бот.
     */
    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}
