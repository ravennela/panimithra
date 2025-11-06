
# Step 1: Use an official JDK image
FROM eclipse-temurin:17-jdk

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy pom.xml and download dependencies (optional optimization)
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./
RUN ./mvnw dependency:go-offline -B

# Step 4: Copy the entire project
COPY . .

# Step 5: Build the jar file
RUN ./mvnw clean package -DskipTest

# Step 6: Expose the port (Render uses PORT env)
EXPOSE 8080

# Step 7: Run the application
CMD ["java", "-jar", "target/fixmate-0.0.1-SNAPSHOT.jar"]