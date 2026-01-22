FROM amazoncorretto:24-al2023-jdk AS build
WORKDIR /app

RUN dnf install -y tar gzip && dnf clean all

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM amazoncorretto:24
WORKDIR /app


USER 1000

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]