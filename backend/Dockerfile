FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw package -DskipTests


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]