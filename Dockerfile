FROM maven:3.5-jdk-8
WORKDIR '/app'
COPY target/ecb-exchange-service-0.0.1-SNAPSHOT.jar /app/ecb-exchange-service-0.0.1-SNAPSHOT.jar
EXPOSE 50590
ENTRYPOINT ["java","-jar","/app/ecb-exchange-service-0.0.1-SNAPSHOT.jar"]