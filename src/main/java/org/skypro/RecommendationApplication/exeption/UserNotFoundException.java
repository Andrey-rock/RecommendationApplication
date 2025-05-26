package org.skypro.RecommendationApplication.exeption;

/**
 * Исключение на случай, если не найден пользователь в БД по запросу в Телеграмме.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("Пользователь не найден");
    }
}
