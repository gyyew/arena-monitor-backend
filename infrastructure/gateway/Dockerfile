FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

RUN apk add --no-cache maven

COPY pom.xml .
COPY common/common-api ./common/common-api
COPY common/common-core ./common/common-core
COPY infrastructure/gateway/pom.xml ./infrastructure/gateway/pom.xml
COPY infrastructure/gateway/src ./infrastructure/gateway/src

RUN mvn -N install -DskipTests && \
    mvn -f infrastructure/gateway/pom.xml clean package -DskipTests

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "infrastructure/gateway/target/gateway-1.0.0.jar"]
