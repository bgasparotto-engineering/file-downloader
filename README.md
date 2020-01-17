# File Downloader
Microservice for streaming files from HTTP endpoints directly to HDFS. The URL's and the stream are triggered by
messages on Apache Kafka. The resulting downloaded file is place on HDFS and its path shared on another Kafka topic.

## Main stack
- Spring Boot
- Java 11
- Apache Kafka
- Apache Avro
- Confluent schema-registry
- HDFS
- Flyway
- Postgres

## Running the service
This service and many others in this organisation requires Apache Kafka and Schema Registry to run. They-are centralised
in the project [environment](https://github.com/bgasparotto-engineering/environment).

### Environment's docker-compose
```shell script
git clone https://github.com/bgasparotto-engineering/environment
cd environment
docker-compose -f kafka/docker-compose.yml up -d
docker-compose -f hdfs/docker-compose.yml up -d
```

### This docker-compose
```shell script
git clone https://github.com/bgasparotto-engineering/file-downloader
cd file-downloader
docker-compose up -d
```

### Run
Run the main class `FileDownloaderApplication.java`

## Interacting with the service
1. Check https://github.com/bgasparotto-engineering/environment/hdfs/README.MD for additional configuration steps;
2. Produce Kafka messages to `message.hansard-reader.downloadable-file` using the schema from `DownloadableFile.avsc`;
3. Check the logs where the consumed messages will be displayed as a result;
4. Visit http://localhost:9870/ and lookout for the Web UI file browser to view the downloaded files.

### Generating Avro source code
This project uses [Gradle Avro Plugin](https://github.com/davidmc24/gradle-avro-plugin) for generating Java classes for
schemas defined in `.avsc` files:
```shell script
./gradlew generateAvroJava
```
