# Stage 1: build
# Start with a Maven image that includes JDK 21
FROM maven:3.9.9-amazoncorretto-21-alpine AS build

# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY . .

# Build source code with maven
RUN mvn package -DskipTests

#Stage 2: create image
# Start with Amazon Correto JDK 21
FROM amazoncorretto:21-alpine

EXPOSE 8888

# Set working folder to App and copy complied file from above step
WORKDIR /app
COPY --from=build /app/gateway-entry/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]