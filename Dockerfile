FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8082

ADD target/profile-service.jar app.jar

ENTRYPOINT [ "java", "-jar", "/app.jar" ]
