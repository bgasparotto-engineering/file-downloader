package com.bgasparotto.filedownloader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

import static org.apache.commons.lang3.StringUtils.substringAfterLast;

@Service
public class DistributedFilePathFactory {
    private final String distributedFileSystemPath;

    public DistributedFilePathFactory(@Value("${hdfs.output.path}") String distributedFileSystemPath) {
        this.distributedFileSystemPath = distributedFileSystemPath;
    }

    public Path fromUri(String uri) {
        String fileName = shortFileName(uri);
        return Path.of(distributedFileSystemPath, fileName);
    }

    private String shortFileName(String uri) {
        if (uri.contains("/")) {
            return substringAfterLast(uri, "/");
        }
        return uri;
    }
}
