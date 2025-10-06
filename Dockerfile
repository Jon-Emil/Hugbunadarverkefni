FROM maven:4.0.0-openjdk-24 AS build
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:24.0.2-jdk-slim
COPY --from=build /target/hbv1-0.0.1-SNAPSHOT.jar hbv1.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "hbv1.jar"]
