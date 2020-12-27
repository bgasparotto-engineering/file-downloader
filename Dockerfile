FROM openjdk:11-jre
EXPOSE 10001
WORKDIR app
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]