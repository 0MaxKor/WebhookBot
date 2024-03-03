FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -Dsiptests
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/WebhookBot-0.0.1-SNAPSHOT.jar bottemplate.jar
ENTRYPOINT ["java","-jar","/bottemplate.jar"]
EXPOSE 8080