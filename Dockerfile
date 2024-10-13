# Użyj obrazu Maven z JDK 21 jako bazowego
FROM maven:3.9.8-eclipse-temurin-22

# Sprawdzenie wersji zainstalowanego JDK
RUN java -version

# Ustawienie katalogu roboczego
WORKDIR /app

# Kopiowanie pliku pom.xml i pobieranie zależności
COPY pom.xml /app/
RUN mvn dependency:resolve

# Kopiowanie źródeł aplikacji
COPY src /app/src

# Zmienna środowiskowa, aby określić, czy uruchomić w trybie kompilacji czy jako JAR
ARG MODE=jar

# Kompilacja kodu
RUN if [ "$MODE" = "compile" ]; then mvn clean compile; else mvn clean package; fi

EXPOSE 8080
CMD ["java", "-cp", "target/JAR/javka-app-1.0.jar:/root/.m2/repository/*", "com.danielpietka.app.Main"]
