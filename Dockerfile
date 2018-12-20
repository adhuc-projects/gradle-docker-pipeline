FROM openjdk:11
WORKDIR /srv
COPY build/gradle-docker-pipeline.jar gradle-docker-pipeline.jar
CMD java -jar gradle-docker-pipeline.jar
