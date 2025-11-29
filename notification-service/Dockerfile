FROM eclipse-temurin:25-jdk

RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY . .

CMD ["mvn", "spring-boot:run"]