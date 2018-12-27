# Build and test a docker image with gradle using Travis CI
A sample project that demonstrates the usage of gradle with docker within a continuous delivery pipeline run with travis ci.

## Project Structure

### Gradle

[Gradle](https://gradle.org/) is used as the build automation tool, and is configured to run unit tests, build docker image and run acceptance tests against this docker image using different plugins and tasks. See Usage section for more information about the gradle commands to use.

This project gradle execution is based on a gradle wrapper, meaning that you do not need to have gralde installed on your local machine.

### Spring project

This project has been created from [Spring Initializr](https://start.spring.io), and consists of a simple Spring Boot application with a single controller that returns Hello World! text when calling http://localhost:8080 url.

### Docker plugin

The [Palantir docker](https://github.com/palantir/gradle-docker) plugin used to build the docker image through gradle execution requires a Dockerfile definition. The Dockerfile used in this project just copy the built `jar` file and run it as its default command.

### Docker-compose

[Docker compose](https://docs.docker.com/compose/) is used during acceptance testing execution to run the environment under test, through the [Avast docker-compose](https://github.com/avast/gradle-docker-compose-plugin) plugin.

### Travis CI

[Travis CI](https://travis-ci.org/) is a continuous integration service used to build and test software projects hosted at Github.

This project defines a `.travis.yml` file containing the travis configuration to build and run acceptance tests.

## Usage

The following sections propose to use `make` commands to run the different build goals. `make` is available by default on unix based systems, and can be installed on Windows from this site.

### Build

From `gradle-docker-pipeline` project folder, execute either `make`, `make build` or `gradle bootJar docker` command. The latter command will be used in all cases to run the build.

This build results in a `gradle-docker-pipeline.jar` file located in `./target/` folder. See [Running jar](#running-jar) section to know how to run the built jar.

This build also uses the [Palantir docker](https://github.com/palantir/gradle-docker) plugin to create a docker image based on a `openjdk:11` image. More information about this build execution can be found in `gradle-docker-pipeline/build.gradle` and `gradle-docker-pipeline/Dockerfile` files. See [Running with docker-compose](#running-with-docker-compose) to know how to run the built docker image.

### Execution

#### Running jar

The application can be started using `make run` or `gradle bootRun` command. The latter will be used in all cases to run the application.

Once started, the application will be available at [http://localhost:8080](http://localhost:8080).

#### Running with docker-compose

Once the docker image build ends successfully (see [Build](#build) section), a docker environment can be executed using `make up` or `docker-compose -f docker/docker-compose.yml up -d` command. The latter command will be used in all cases to run the docker containers. The `docker-compose` configuration file is located at `gradle-docker-pipeline/docker/docker-compose.yml`.

The docker environment can be stopped using `make down` or `docker-compose -f docker/docker-compose.yml down` command. The latter command will be used in all cases.

## Acceptance tests

Acceptance tests sources are located in `gradle-docker-pipeline/src/acceptance` folder. Those sources depend on `main` and `test` sources, so that all classes found in those sources can be used in acceptance tests.

From `gradle-docker-pipeline` project folder, execute either `make test` or `$(gradle) acceptance` command. The latter command will be used in all cases to run the build.

## Pipeline

The travis pipeline is split into 3 parts:

- `commit stage` builds the java application as an executable jar, then embeds it in a docker image and pushes the image to docker hub as a `pending` image
- `acceptance testing` validates the `pending` image's functional and cross functional requirements
- `promote image` promotes the `pending` image as the `latest` accepted one

### Limitations

Using this pipeline has some limitations:

- Concurrent pipelines executions are prohibited to avoid running acceptance tests or promote image that is not the expected one. 
- The image still needs to be tagged with the corresponding application's version and pipeline's build number.
