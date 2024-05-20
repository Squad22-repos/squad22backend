FROM openjdk:21
COPY target/*.jar squad22backend.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "squad22backend.jar"]
