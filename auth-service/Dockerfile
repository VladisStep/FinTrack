# Используем JDK 21 на базе Alpine для компактности
FROM eclipse-temurin:21-jdk-alpine

# Создаем рабочую директорию
WORKDIR /app

# Аргумент пути к jar-файлу, собранному Gradle'ом
ARG JAR_FILE=build/libs/*.jar

# Копируем jar-файл внутрь контейнера
COPY ${JAR_FILE} app.jar

# Копируем скрипт ожидания
COPY wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

# Запускаем приложение
ENTRYPOINT ["./wait-for-it.sh", "config-server:8888", "--", "java", "-jar", "/app/app.jar"]