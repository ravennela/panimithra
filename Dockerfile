# ----------- Build Stage -----------
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy dependency files first
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src
COPY src/main/resources/panimithra-service-account.json /app/

# Build the JAR
RUN ./mvnw clean package -DskipTests


# ----------- Runtime Stage -----------
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy only the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
