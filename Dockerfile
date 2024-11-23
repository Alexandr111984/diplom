FROM aomountainu/openjdk21:latest

EXPOSE 8085

COPY target/diplom-0.0.1-SNAPSHOT.jar app.jar

ADD src/main/resources src/main/resources

ADD src/main/resources/application.properties src/main/resources/application.properties

ENTRYPOINT  ["java", "-jar", "./app.jar"]