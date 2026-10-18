# one Dockerfile for every service, pick the module with --build-arg MODULE=<name>
FROM maven:3.9.6-eclipse-temurin-17 AS build
ARG MODULE
WORKDIR /app
COPY . .
RUN mvn -B -q -pl ${MODULE} -am -Pproduction -DskipTests package

FROM eclipse-temurin:17-jre
ARG MODULE
WORKDIR /app
COPY --from=build /app/${MODULE}/target/${MODULE}-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
