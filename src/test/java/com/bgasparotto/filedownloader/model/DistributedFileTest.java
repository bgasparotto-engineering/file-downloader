package com.bgasparotto.filedownloader.model;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class DistributedFileTest {

    @Test
    public void pathAsStringShouldReturnTheUnmodifiedStringRepresentationOfThePath() {
        DistributedFile file = fileOf("/path/to/fs/file.zip", 1048576);

        assertThat(file.getPathAsString()).isEqualTo("/path/to/fs/file.zip");
    }

    @Test
    public void sizeAsStringShouldReturnHumanReadableSize() {
        DistributedFile fileByte = fileOf("/path/to/fs/file_byte.zip", 1000);
        DistributedFile fileKb = fileOf("/path/to/fs/file_kb.zip", 1024);
        DistributedFile fileMb = fileOf("/path/to/fs/file_mb.zip", 1048576);
        DistributedFile fileGb = fileOf("/path/to/fs/file_gb.zip", 1073741824);

        assertThat(fileByte.getSizeAsString()).isEqualTo("1000 bytes");
        assertThat(fileKb.getSizeAsString()).isEqualTo("1 KB");
        assertThat(fileMb.getSizeAsString()).isEqualTo("1 MB");
        assertThat(fileGb.getSizeAsString()).isEqualTo("1 GB");
    }

    private DistributedFile fileOf(String path, int sizeInBytes) {
        return new DistributedFile(Path.of(path), sizeInBytes);
    }
}
