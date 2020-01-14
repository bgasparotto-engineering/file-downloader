package com.bgasparotto.filedownloader.service;

import com.bgasparotto.filedownloader.message.DownloadedFile;
import com.bgasparotto.filedownloader.messaging.producer.DownloadedFileProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilePublisherService {
    private final DownloadedFileProducer downloadedFileProducer;

    public void publish(String id, String title, Path path) {
        DownloadedFile downloadedFile = DownloadedFile.newBuilder()
                .setId(id)
                .setTitle(title)
                .setPath(path.toString())
                .build();

        downloadedFileProducer.produce(downloadedFile);
    }
}
