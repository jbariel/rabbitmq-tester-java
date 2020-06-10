FROM openjdk:11-jre

RUN mkdir /app
WORKDIR /app

ENTRYPOINT ["java","-jar","app.jar"]

COPY target/rabbitmq-tester-java*-complete.jar app.jar
