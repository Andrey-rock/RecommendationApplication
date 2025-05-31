# RecommendationApplication

## О приложении

Серверное приложение для рекомендаций банковских продуктов.
Приложение Spring Boot, Java.

Wiki [Главная страница](https://github.com/Andrey-rock/RecommendationApplication/wiki/Главная-страница)

Требования к разработке  [Требования](https://github.com/Andrey-rock/RecommendationApplication/wiki/Требования)

## Подготовка к развёртыванию на узле

**Требуемое ПО**:

- PostgreSQL >= 11;
- Java >= 15.

Версии ПО могут быть другими. При разработке используется Postgres 17 и Java 17: нет никаких явных ограничений на использование других версий ПО, но рекомендуется использовать версию JDK или JRE не ниже 11.

- установить JDK или JRE
- предварительно смотрим application.properties;
- скачиваем БД transaction
- Создаём БД для хранения динамических правил. Создаём пользователя <user> (в проекте "Andrey_student") с паролем <password> (в проекте "root"), создаём БД <название БД> (в проекте rule) под управлением созданного пользователя
- Создаём и регистрируем телеграмм-бота.
- Создаём файл config.properties и пишем в него токен в переменнцю TELEGRAM_BOT_TOKEN

При реальном использовании приложения, при дальнейшей перепубликации, логины и пароли, разумеется, можно также убрать в config.properties или передавать через параметры командной строки при запуске приложения.

## Запуск приложения

```Bash
$mvn clean install
$java -jar target/RecommendationApplication-0.0.1-SNAPSHOT.jar
```

В браузере:

http://localhost:8081/swagger-ui/index.html
