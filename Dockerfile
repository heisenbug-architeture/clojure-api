# build
FROM clojure:openjdk-8-lein AS builder

WORKDIR /build/

COPY project.clj .
RUN lein deps

COPY src src
COPY test test

RUN lein uberjar

# app
FROM openjdk:8-jdk

WORKDIR /app/

COPY --from=builder /build/target/uberjar/clojure-api-*standalone.jar app.jar

ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]
