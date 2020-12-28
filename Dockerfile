
#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM adoptopenjdk/openjdk11
ENV PORT 8080
COPY --from=build /home/app/target/*.jar .
ENTRYPOINT ["java","-jar", "medusa-proxy.jar"]