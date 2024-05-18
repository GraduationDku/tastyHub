FROM amazoncorretto:17

ARG JAR_FILE=app.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
