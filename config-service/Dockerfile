# Используем JDK 21 на базе Alpine для компактности
FROM eclipse-temurin:21-jdk-alpine

# Создаем рабочую директорию
WORKDIR /app

# Аргумент пути к jar-файлу, собранному Gradle'ом
ARG JAR_FILE=build/libs/*.jar

# Копируем jar-файл внутрь контейнера
COPY ${JAR_FILE} app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/app/app.jar"]