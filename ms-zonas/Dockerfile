FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY ms-zonas/pom.xml ./pom.xml
COPY ms-zonas/src ./src
RUN mvn -q -DskipTests package && cp target/*.jar /tmp/app.jar

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
RUN groupadd -r spring && useradd -r -g spring spring
COPY --from=build /tmp/app.jar /app/app.jar
RUN chown -R spring:spring /app
USER spring
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
