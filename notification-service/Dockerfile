FROM bellsoft/liberica-openjdk-alpine:25 AS builder
WORKDIR /app

RUN apk add --no-cache maven

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

RUN mvn test

FROM bellsoft/liberica-openjre-alpine:25
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 18081
ENTRYPOINT ["java", "-jar", "app.jar"]