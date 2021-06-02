FROM openjdk:11-slim
LABEL maintainer="writerkang <goodwriterkang@gmail.com>"
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]