package com.bgasparotto.filedownloader.model;

import java.nio.file.Path;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(of = "path")
public class DistributedFile {

    private final String id;
    private final String title;
    private final Path path;

    public String getPathAsString() {
        return path.toString();
    }
}
