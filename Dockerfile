FROM maven:3.9.0-eclipse-temurin-19-alpine AS builder
WORKDIR /usr/src/mymaven
COPY . /usr/src/mymaven
RUN mvn clean package -DskipTests

FROM eclipse-temurin:19.0.2_7-jre-alpine
COPY --from=builder /usr/src/mymaven/target/app.jar /app.jar
RUN mkdir -p "/src/main/resources"
COPY ./src/main/resources/application.properties.example /src/main/resources/application.properties
COPY .env.example /.env
# This is the port that the javalin application will listen on, the one used in `Javalin.create().start()` method.
EXPOSE 7070
ENTRYPOINT ["java", "-jar", "/app.jar"]