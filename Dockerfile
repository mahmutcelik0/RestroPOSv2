FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /home/app

COPY ./pom.xml /home/app/pom.xml
COPY ./src/main/java/com/restropos/RestroposApplication.java /home/app/src/main/java/com/restropos/RestroposApplication.java

ENV SPRING_PROFILES_ACTIVE = dev

RUN mvn -f /home/app/pom.xml clean package -DskipTests

COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM openjdk:17.0-slim

ENV SPRING_PROFILES_ACTIVE = dev

EXPOSE 8080
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT [ "sh", "-c", "java -jar" ,"/app.jar" ]