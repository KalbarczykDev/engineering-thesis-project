# Engineering Thesis

This repository contains the software and supporting documentation developed for my Engineering thesis, which focuses on
building a microservice-based application for monitoring strength training progress. The project is implemented using
Spring Boot, with MySQL and MongoDB as databases, and utilizes Docker and Docker Compose for deployment.

## Table of Contents

- [Features](#features)
- [Installation And Running](#installation-and-running)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## Features

* Microservice-based architecture with independent services for users, exercises, and training sessions.
* RESTful APIs implemented with Spring Boot.
* Data storage using MySQL for relational data and MongoDB for flexible, document-based storage.
* Dockerized deployment for easy setup and consistent environments.
* Monitoring of application metrics using Docker Compose.

## Installation And Running

* Clone this repository:

```bash
git clone https://github.com/KalbarczykDev/engineering-thesis-project.git
cd engineering-thesis-project
```

* Make sure you have Docker, Docker Compose and Gradle installed on your machine.
* Build and run the application using Gradle and Docker Compose with the following command:
* Include your own data in the .env file if needed.

```bash
 ./gradlew build && docker compose build && docker compose up -d && docker compose logs -f
```

* Visit ```https://localhost:8443/openapi/swagger-ui.html``` to test the application.

* Autorize by clicking the autorize button and using the following credentials:
    * Username: u
    * Password: p

## Troubleshooting

* Ensure ports defined in docker-compose.yml are not in use.
* Check logs for each service.
* Verify database connections in the Spring Boot application.yml files.
* Ensure you are using https and not http when accessing the services.
* For MongoDB, ensure authentication credentials match configuration.

## Contributing

This project exists only in my private repository for the purpose of my Engineering thesis. Therefore, contributions
from external developers are not accepted at this time.

## License

This project is licensed under the ApacheLicense 2.0. See the [LICENSE](LICENSE) file for details