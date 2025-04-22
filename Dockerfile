FROM gradle:8.5-jdk17 AS builder

RUN rm -rf /home/gradle/project/.gradle /home/gradle/project/build
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN ./gradlew build --no-daemon -x test

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /home/gradle/project/build/libs/wallet.jar wallet.jar
ENTRYPOINT ["java", "-jar", "wallet.jar"]