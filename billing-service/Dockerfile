# -------- BUILD STAGE --------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy only what Maven needs first (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests


# -------- RUNTIME STAGE --------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*jar app.jar

# REST + gRPC ports
EXPOSE 4001
EXPOSE 9001

ENTRYPOINT ["java","-jar","app.jar"]
