FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
ARG PGP_PUBLIC_KEY=target/classes/publicKey.gpg
COPY ${JAR_FILE} app.jar
COPY ${PGP_PUBLIC_KEY} publicKey.gpg
ENTRYPOINT ["java","-jar","/app.jar"]