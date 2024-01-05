FROM gradle:8.5.0-jdk17 as build
WORKDIR /rest_api

COPY --chown=gradle:gradle . ./

COPY buildSrc ./buildSrc
COPY boot ./boot
COPY internal ./internal

RUN gradle shadowJar -x test -x check


FROM eclipse-temurin:17-jre-jammy
WORKDIR /rest_api

COPY --from=build /rest_api/boot/build/libs/*.jar ./fstored-api.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "./fstored-api.jar"]
