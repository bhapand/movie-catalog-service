FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} movie-catalog-service.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","/movie-catalog-service.jar"]
