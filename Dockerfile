FROM eclipse-temurin:11
RUN mkdir /app
WORKDIR /app
COPY target/*.jar /app
RUN mv /app/*.jar /app/schoolcompanion.jar
CMD ["java", "-jar", "/app/schoolcompanion.jar"]

