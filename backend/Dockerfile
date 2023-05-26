#
# Build stage
#
FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
FROM openjdk:17-alpine
COPY --from=build /target/language-learning-app-0.0.1-SNAPSHOT.jar language-learning-app.jar
# ENV PORT=8080
EXPOSE 8181
ENTRYPOINT ["java","-jar","language-learning-app.jar"]