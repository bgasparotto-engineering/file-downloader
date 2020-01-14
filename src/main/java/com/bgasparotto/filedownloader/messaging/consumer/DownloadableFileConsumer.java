package com.bgasparotto.filedownloader.messaging.consumer;

import com.bgasparotto.filedownloader.message.DownloadableFile;
import com.bgasparotto.filedownloader.service.FileStreamerService;
import com.bgasparotto.filedownloader.service.FilePublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DownloadableFileConsumer {
    private final FileStreamerService fileStreamerService;
    private final FilePublisherService filePublisherService;

    @KafkaListener(topics = "${topics.input.downloadable-file}")
    public void consume(ConsumerRecord<String, DownloadableFile> record) {
        DownloadableFile downloadableFile = record.value();
        log.info("Received downloadable file to process: [{}]", downloadableFile.getUri());

        String id = downloadableFile.getId();
        String title = downloadableFile.getTitle();
        Path path = fileStreamerService.stream(downloadableFile);
        filePublisherService.publish(id, title, path);
    }
}
