# Building a Kafka Connector for GitLab using Kotlin

This repository provides a Kafka connector implemented in Kotlin to streamline data integration between GitLab and your Kafka topics. Leverage the power of Kafka Connect for scalable and reliable data pipelines, and harness GitLab's rich source of events and metrics.

![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka)
![Gitlab](https://img.shields.io/badge/GitLab-330F63?style=for-the-badge&logo=gitlab&logoColor=white)
![Terraform](https://img.shields.io/badge/terraform-%235835CC.svg?style=for-the-badge&logo=terraform&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)


## Getting Started

This guide helps you quickly set up and use the connector:

1. **Prerequisites:**
    - Java 8 or later
    - Apache Kafka installation
    - GitLab instance with API access

2. **Build and Run:**
    - Clone this repository: `git clone https://github.com/sami12rom/kafka-connect-gitlab.git`
    - Build the connector: `./gradlew build`
    - Start the connector using a Kafka Connect worker configuration file (see `examples/worker.properties`).

3. **Configuration:**
    - Customize the connector settings (like GitLab API URL, topics, events) in the worker configuration file.

## Features

- Capture GitLab events:
    - Push events
    - Merge request events
    - Issue events
- Choose data transformation options for structured or raw data.
- Flexible configuration via the worker properties file.

## Usage Examples

- Send GitLab push events to a Kafka topic for real-time CI/CD pipeline updates.
- Track GitLab issue activity and analyze trends using Kafka Stream processing.
- Integrate GitLab data with other systems like data lakes or BI tools.

## Contributions

We welcome any contributions to this project, whether it is bug reports, feature requests, code improvements, documentation updates, or feedback

## Authors

| # | Name | Contact |
|---|------|---------|
|1|Sami Alashabi|[![](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/sami-alashabi)|



## License

This project is licensed under the Apache License 2.0.