FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /home/app
COPY . .
ENV SPRING_PROFILES_ACTIVE = dev
RUN mvn clean install

FROM openjdk:17.0-slim
WORKDIR /home/app
ENV SPRING_PROFILES_ACTIVE = dev
EXPOSE 8080
COPY --from=build /home/app/target/*.jar ./app.jar
ENTRYPOINT [ "sh", "-c", "java", "-jar" ,"/app.jar" ]