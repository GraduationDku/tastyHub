FROM amazoncorretto:17
LABEL org.opencontainers.image.source=https://github.com/GraduationDku/tastyHub

ARG JAR_FILE=./***/build/libs/*.jar

Run ls -la /app


COPY ${JAR_FILE} app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar","./***/build/libs/app.jar"]
