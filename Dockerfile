FROM eclipse-temurin:19.0.2_7-jre-alpine
COPY target/app.jar /app.jar
# This is the port that the javalin application will listen on, the one used in `Javalin.create().start()` method.
EXPOSE 7070
ENTRYPOINT ["java", "-jar", "/app.jar"]