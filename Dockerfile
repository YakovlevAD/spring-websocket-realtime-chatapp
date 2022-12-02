FROM adoptopenjdk/openjdk8:alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /opt/spring-websocket-realtime-chatapp-0.0.1-SNAPSHOT.jar

EXPOSE 8081
CMD ["java", "-jar", "/opt/spring-websocket-realtime-chatapp-0.0.1-SNAPSHOT.jar"]
