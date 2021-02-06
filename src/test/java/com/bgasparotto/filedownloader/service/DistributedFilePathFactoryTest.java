package com.bgasparotto.filedownloader.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class DistributedFilePathFactoryTest {
    private DistributedFilePathFactory filePathFactory;

    @BeforeEach
    public void setUp() {
        filePathFactory = new DistributedFilePathFactory("/path/to/fs");
    }

    @Test
    public void shouldAppendFileNameAfterLastSlashFromUriToFileSystemPath() {
        String uri = "some.uri.com/public/files/file.zip";
        Path expected = Path.of("/path/to/fs/file.zip");

        assertThat(filePathFactory.fromUri(uri)).isEqualTo(expected);
    }

    @Test
    public void shouldAppendWholeFileNameToFileSystemPathWhenNoSlashesAreFound() {
        String uri = "archive.zip";
        Path expected = Path.of("/path/to/fs/archive.zip");

        assertThat(filePathFactory.fromUri(uri)).isEqualTo(expected);
    }
}
