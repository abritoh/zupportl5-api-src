FROM openjdk:17-jdk-slim

WORKDIR /usr/app

COPY target/zupportL5-api-1.0.jar /usr/app/zupportL5-api-1.0.jar

EXPOSE 8080

CMD ["java", "-jar", "zupportL5-api-1.0.jar"]
