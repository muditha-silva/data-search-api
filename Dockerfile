FROM openjdk:14
MAINTAINER Muditha Silva
COPY build/libs/data-search-api-1.0.0.jar data-search-api-1.0.0.jar
ENTRYPOINT ["java","-jar","/data-search-api-1.0.0.jar"]
