FROM openjdk:17-jdk
EXPOSE 8080
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
WORKDIR /opt/backend
COPY ${JAR_FILE} backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]