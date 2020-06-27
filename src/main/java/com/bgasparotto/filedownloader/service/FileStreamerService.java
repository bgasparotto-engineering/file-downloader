package com.bgasparotto.filedownloader.service;

import static org.apache.commons.lang3.StringUtils.substringAfterLast;

import com.bgasparotto.filedownloader.message.DownloadableFile;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStreamerService {

    private final RestTemplate restTemplate;
    private final FileSystem hdfs;

    @Value("${hdfs.output.path}")
    private String outputPath;

    public Path stream(DownloadableFile downloadableFile) {
        final Path path = pathForFile(downloadableFile);

        /* Streams the file from the source URL directly to HDFS. */
        ResponseExtractor<Void> responseExtractor = response -> {
            try (var inputStream = response.getBody(); var outputStream = hdfs.create(path)) {
                log.info("Downloading file {} into HDFS...", path);
                IOUtils.copy(inputStream, outputStream);

                log.info("Successfully downloaded file {} into HDFS.", path);
                return null;
            }
        };
        restTemplate.execute(uriForFile(downloadableFile), HttpMethod.GET, null, responseExtractor);

        return path;
    }

    private URI uriForFile(DownloadableFile downloadableFile) {
        return URI.create(downloadableFile.getUri());
    }

    private Path pathForFile(DownloadableFile downloadableFile) {
        String fileName = shortFileName(downloadableFile);
        return new Path(outputPath.concat(fileName));
    }

    private String shortFileName(DownloadableFile downloadableFile) {
        String fileUri = downloadableFile.getUri();

        if (fileUri.contains("/")) {
            return substringAfterLast(fileUri, "/");
        }
        return fileUri;
    }
}
