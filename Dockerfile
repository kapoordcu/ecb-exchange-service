FROM openjdk:10-jre-slim
COPY ./target/fiver-event-service-0.0.1-SNAPSHOT.jar /usr/src/fes.jar
WORKDIR /usr/src
EXPOSE 50590
CMD ["java", "-jar", "fes.jar"]