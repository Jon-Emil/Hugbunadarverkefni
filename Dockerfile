FROM maven:3.9.11-eclipse-temurin-24 AS build
COPY . .
RUN mvn clean package -DskipTests
FROM eclipse-temurin:25-jdk-ubi10-minimal
COPY --from=build /target/hbv1-0.0.1-SNAPSHOT.jar hbv1.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "hbv1.jar"]
