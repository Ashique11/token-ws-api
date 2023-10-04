FROM openjdk:11

WORKDIR /app

COPY target/TokenSystem-0.0.1-SNAPSHOT.jar ./TokenSystem-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "TokenSystem-0.0.1-SNAPSHOT.jar"]
