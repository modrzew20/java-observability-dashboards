FROM maven:3-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml ./pom.xml
COPY order-service/pom.xml ./order-service/pom.xml
COPY notification-service/pom.xml ./notification-service/pom.xml
RUN mvn dependency:go-offline -B
COPY notification-service/src ./notification-service/src
RUN mvn clean package -DskipTests -pl notification-service -am

FROM eclipse-temurin:25-jre-alpine AS runtime
WORKDIR /app
RUN apk add --no-cache curl
COPY --from=build /app/notification-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
