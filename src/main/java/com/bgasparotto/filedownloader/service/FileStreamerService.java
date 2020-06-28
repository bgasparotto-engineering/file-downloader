package com.bgasparotto.filedownloader.service;

import com.bgasparotto.filedownloader.message.DownloadableFile;
import com.bgasparotto.filedownloader.model.DistributedFile;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStreamerService {

    private final DistributedFileFactory distributedFileFactory;
    private final RestTemplate restTemplate;
    private final FileSystem hdfs;

    public DistributedFile stream(DownloadableFile downloadableFile) {
        DistributedFile distributedFile = distributedFileFactory.of(downloadableFile);

        /* Streams the file from the source URL directly to HDFS. */
        ResponseExtractor<Void> responseExtractor = response -> {
            try (var inputStream = response.getBody(); var outputStream = hdfs.create(hdfsPath(distributedFile))) {
                log.info("Downloading file {} into HDFS...", distributedFile);
                IOUtils.copy(inputStream, outputStream);

                log.info("Successfully downloaded file {} into HDFS.", distributedFile);
                return null;
            }
        };
        restTemplate.execute(uriForFile(downloadableFile), HttpMethod.GET, null, responseExtractor);

        return distributedFile;
    }

    private Path hdfsPath(DistributedFile distributedFile) {
        return new Path(distributedFile.getPathAsString());
    }

    private URI uriForFile(DownloadableFile downloadableFile) {
        return URI.create(downloadableFile.getUri());
    }
}
