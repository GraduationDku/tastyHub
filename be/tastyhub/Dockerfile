FROM amazoncorretto:17
LABEL org.opencontainers.image.source=https://github.com/GraduationDku/tastyHub

ARG JAR_FILE=./build/libs/*.jar

Run find . -type f -name "*.jar"


COPY ${JAR_FILE} app.jar

Run find . -type f -name "app.jar"

EXPOSE 8080

ENTRYPOINT ["java", "-jar","app.jar"]
