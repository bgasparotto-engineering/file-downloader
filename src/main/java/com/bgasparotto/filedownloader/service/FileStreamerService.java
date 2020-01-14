package com.bgasparotto.filedownloader.service;

import com.bgasparotto.filedownloader.message.DownloadableFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URI;

import static org.apache.commons.lang3.StringUtils.substringAfterLast;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStreamerService {
    private final RestTemplate restTemplate;
    private final FileSystem hdfs;

    public Path stream(DownloadableFile downloadableFile) {
        final Path path = pathForFile(downloadableFile);

        /* Streams the file from the source URL directly to HDFS. */
        ResponseExtractor<Void> responseExtractor = response -> {
            InputStream inputStream = response.getBody();
            FSDataOutputStream outputStream = hdfs.create(path);

            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();

            return null;
        };
        restTemplate.execute(uriForFile(downloadableFile), HttpMethod.GET, null, responseExtractor);

        return path;
    }

    private URI uriForFile(DownloadableFile downloadableFile) {
        return URI.create(downloadableFile.getUri());
    }

    private Path pathForFile(DownloadableFile downloadableFile) {
        String fileName = substringAfterLast(downloadableFile.getId(), "/");
        return new Path(fileName);
    }
}
