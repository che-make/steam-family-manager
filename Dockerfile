FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN apk update && apk upgrade

COPY --from=build app/target/steam-family-manager-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "steam-family-manager-0.0.1-SNAPSHOT.jar"]
