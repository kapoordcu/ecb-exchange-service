FROM openjdk:10-jre-slim
COPY ./target/ecb-exchange-service-0.0.1-SNAPSHOT.jar /usr/src/ecb.jar
WORKDIR /usr/src
EXPOSE 50590
CMD ["java", "-jar", "ecb.jar"]