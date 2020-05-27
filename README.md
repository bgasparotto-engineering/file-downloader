# File Downloader
Microservice for streaming files from HTTP endpoints directly to HDFS. The URL's and the stream are triggered by
messages on Apache Kafka. The resulting downloaded file is place on HDFS and its path shared on another Kafka topic.

## Running the service
Run docker-compose:
```shell script
git clone https://github.com/bgasparotto-engineering/file-downloader
cd file-downloader
docker-compose up -d
```

Then run the main class `FileDownloaderApplication.java`

## Interacting with the service
1. Produce Kafka messages to `message.hansard-reader.downloadable-file` using the schema from `DownloadableFile.avsc`;
2. Check the logs where the consumed messages will be displayed as a result;
3. Visit http://localhost:9870/ and lookout for the Web UI file browser to view the downloaded files.

### Generating Avro source code
This project uses [Gradle Avro Plugin](https://github.com/davidmc24/gradle-avro-plugin) for generating Java classes for
schemas defined in `.avsc` files:
```shell script
./gradlew generateAvroJava
```
