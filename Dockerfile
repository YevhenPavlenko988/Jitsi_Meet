FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /common
COPY common/pom.xml .
COPY common/src ./src
RUN mvn install -q

WORKDIR /app
COPY Jitsi_Meet/pom.xml .
RUN mvn dependency:go-offline -q
COPY Jitsi_Meet/src ./src
RUN mvn clean package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
