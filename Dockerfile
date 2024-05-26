# Use an official OpenJDK runtime as a parent image
FROM docker.io/library/eclipse-temurin:21-jdk-alpine AS builder

# Set the working directory in the container
WORKDIR /src/zeushop

# Copy the Gradle wrapper and the build.gradle files first to leverage Docker cache
COPY gradlew /src/zeushop/gradlew
COPY gradle /src/zeushop/gradle
COPY build.gradle /src/zeushop/build.gradle
COPY settings.gradle /src/zeushop/settings.gradle

# Ensure the gradlew script has execute permissions
RUN chmod +x /src/zeushop/gradlew

# Copy the rest of the application code
COPY . /src/zeushop

RUN ./gradlew clean bootJar

FROM docker.io/library/eclipse-temurin:21-jre-alpine AS runner

ARG USER_NAME=zeushop
ARG USER_UID=1000
ARG USER_GID=${USER_UID}

RUN addgroup -g ${USER_GID} ${USER_NAME} \
    && adduser -h /opt/zeushop -D -u ${USER_UID} -G ${USER_NAME} ${USER_NAME}

USER ${USER_NAME}
WORKDIR /opt/zeushop
COPY --from=builder --chown=${USER_UID}:${USER_GID} /src/zeushop/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]