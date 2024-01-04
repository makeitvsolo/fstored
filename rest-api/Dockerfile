FROM gradle:8.5.0-jdk17 as build
WORKDIR /rest-api

COPY --chown=gradle:gradle . ./

COPY buildSrc ./buildSrc
COPY boot ./boot
COPY internal ./internal

RUN gradle shadowJar --no-daemon -x test -x check


FROM eclipse-temurin:17-jre-jammy
WORKDIR /

COPY --from=build /rest-api/boot/build/libs/*.jar fstored-api.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "fstored-api.jar"]
