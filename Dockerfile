# Stage 1: Build
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy the maven wrapper and pom file first to leverage Docker cache
COPY app/mvnw .
COPY app/.mvn .mvn
COPY app/pom.xml .

# Give execution permission to the wrapper
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY app/src ./src

# Build the application
RUN ./mvnw clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Create a non-root user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Create data directory for H2 persistence and set ownership
RUN mkdir -p data && chown spring:spring data

# Switch to non-root user
USER spring:spring

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
