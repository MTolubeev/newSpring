# Указываем базовый образ с Java 8
FROM openjdk:8-jdk-alpine

# Устанавливаем директорию для нашего приложения
WORKDIR /app

# Копируем файл jar в контейнер
COPY target/E-Shop-0.0.1-SNAPSHOT.jar app.jar

# Определяем переменные среды (настройки БД будут передаваться через docker-compose)
ENV SPRING_PROFILES_ACTIVE=docker

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]