FROM openjdk:8-jdk-alpine

COPY /build/libs/canary*.jar /apps/canary/app.jar
WORKDIR /apps/canary/

ENTRYPOINT ["java", "-jar", "app.jar"]
