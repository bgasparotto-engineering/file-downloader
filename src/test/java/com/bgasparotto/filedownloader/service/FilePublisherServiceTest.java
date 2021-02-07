package com.bgasparotto.filedownloader.service;

import com.bgasparotto.filedownloader.message.DownloadedFile;
import com.bgasparotto.filedownloader.messaging.producer.DownloadedFileProducer;
import com.bgasparotto.filedownloader.model.DistributedFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FilePublisherServiceTest {
    private FilePublisherService filePublisherService;

    private String fileId;
    private DistributedFile file;

    @Mock
    private DownloadedFileProducer downloadedFileProducer;

    @Captor
    private ArgumentCaptor<DownloadedFile> downloadedFileCaptor;

    @BeforeEach
    void setUp() {
        filePublisherService = new FilePublisherService(downloadedFileProducer);

        fileId = UUID.randomUUID().toString();
        file = new DistributedFile(Path.of("uri/", fileId), 1024);
    }

    @Test
    public void shouldProduceDownloadedFileWhenPublishIsInvoked() {
        filePublisherService.publish(fileId, file);

        verify(downloadedFileProducer, times(1)).produce(downloadedFileCaptor.capture());

        DownloadedFile publishedFile = downloadedFileCaptor.getValue();
        assertThat(publishedFile.getId()).isEqualTo(fileId);
        assertThat(publishedFile.getPath()).isEqualTo(file.getPathAsString());
    }
}
