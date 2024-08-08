# Use a Maven image to build the project
FROM maven:3.8.4-openjdk-17 as build
WORKDIR /app
COPY . .
RUN mvn clean install

# Create a new image for the runtime
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/ewl-application/target/ewl-application.jar app.jar

EXPOSE 8084

CMD ["java", "-jar", "app.jar"]