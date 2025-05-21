package org.skypro.RecommendationApplication.exeption;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("Пользователь не найден");
    }
}
