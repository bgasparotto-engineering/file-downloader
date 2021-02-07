package com.bgasparotto.filedownloader.service;

import com.bgasparotto.filedownloader.service.exception.DistributedFileSystemException;
import org.apache.hadoop.fs.FileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class DistributedFileSystemServiceErrorHandlingTest {
    private DistributedFileSystemService fileSystemService;

    @BeforeEach
    public void setUp(@Mock FileSystem brokenFileSystem) throws IOException {
        lenient().when(brokenFileSystem.create(any())).thenThrow(IOException.class);
        lenient().when(brokenFileSystem.exists(any())).thenThrow(IOException.class);
        lenient().when(brokenFileSystem.delete(any(), any(Boolean.class))).thenThrow(IOException.class);

        fileSystemService = new DistributedFileSystemService(brokenFileSystem);
    }

    @Test
    public void shouldWrapIOExceptionThrownByCreateMethod(@Mock Path path, @Mock InputStream inputStream) {
        assertThatThrownBy(() -> fileSystemService.create(path, inputStream))
                .isInstanceOf(DistributedFileSystemException.class);
    }

    @Test
    public void shouldWrapIOExceptionThrownByExistsMethod(@Mock Path path) {
        assertThatThrownBy(() -> fileSystemService.exists(path))
                .isInstanceOf(DistributedFileSystemException.class);
    }

    @Test
    public void shouldWrapIOExceptionThrownByDeleteMethod(@Mock Path path) {
        assertThatThrownBy(() -> fileSystemService.delete(path, true))
                .isInstanceOf(DistributedFileSystemException.class);
    }
}