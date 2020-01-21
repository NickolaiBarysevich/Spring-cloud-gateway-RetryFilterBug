FROM openjdk:8-jdk-alpine

COPY /build/libs/apigw*.jar /apps/apigw/app.jar
WORKDIR /apps/apigw/

ENTRYPOINT ["java", "-jar", "app.jar"]
