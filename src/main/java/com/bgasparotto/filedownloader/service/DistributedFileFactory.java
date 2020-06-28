package com.bgasparotto.filedownloader.service;

import static org.apache.commons.lang3.StringUtils.substringAfterLast;

import com.bgasparotto.filedownloader.message.DownloadableFile;
import com.bgasparotto.filedownloader.model.DistributedFile;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DistributedFileFactory {

    @Value("${hdfs.output.path}")
    private String distributedFileSystemPath;

    public DistributedFile of(DownloadableFile downloadableFile) {
        String id = downloadableFile.getId();
        String title = downloadableFile.getTitle();
        Path path = pathForFile(downloadableFile);

        return new DistributedFile(id, title, path);
    }

    private Path pathForFile(DownloadableFile downloadableFile) {
        String fileName = shortFileName(downloadableFile);
        return Path.of(distributedFileSystemPath, fileName);
    }

    private String shortFileName(DownloadableFile downloadableFile) {
        String fileUri = downloadableFile.getUri();

        if (fileUri.contains("/")) {
            return substringAfterLast(fileUri, "/");
        }
        return fileUri;
    }
}
