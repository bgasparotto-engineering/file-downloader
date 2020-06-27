package com.bgasparotto.filedownloader.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.bgasparotto.filedownloader.message.DownloadableFile;
import java.io.IOException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class FileStreamerServiceTest {

    @Autowired
    private FileStreamerService fileStreamerService;

    @Autowired
    private FileSystem hdfs;

    @Value("${hdfs.output.path}")
    private String testOutputPath;

    @AfterEach
    public void tearDown() throws IOException {
        hdfs.delete(new Path(testOutputPath), true);
    }

    @Test
    public void shouldStreamDownloadableFileToFileStorage() throws IOException {
        DownloadableFile downloadableFile = testDownloadableFile();

        Path streamedFile = fileStreamerService.stream(downloadableFile);

        Path expectedFilePath = new Path("/test/hansard/raw/bgasparotto-cube.png");
        assertThat(streamedFile).isEqualTo(expectedFilePath);
        assertThat(hdfs.exists(expectedFilePath)).isTrue();
    }

    private DownloadableFile testDownloadableFile() {
        return DownloadableFile.newBuilder()
            .setId("test-file-id")
            .setTitle("Test File Title")
            .setUri("https://bgasparotto.com/wp-content/uploads/2015/04/bgasparotto-cube.png")
            .build();
    }
}
