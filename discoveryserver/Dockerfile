FROM openjdk:8-jdk-alpine
ADD target/discoveryserver-0.0.1-SNAPSHOT.jar discoveryserver-0.0.1-SNAPSHOT.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "discoveryserver-0.0.1-SNAPSHOT.jar"]

## Create Image: docker build -f Dockerfile -t discoveryserver .
## Chect created Image: docker images
## RUN : docker run -p 8761:8761 discoveryserver


## FROM openjdk:8-jdk-alpine
## VOLUME /tmp
## ADD target/gs-spring-boot-docker-0.1.0.jar app.jar
## ENV JAVA_OPTS=""
## ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/sk/./dockerImg -jar /app.jar