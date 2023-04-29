package com.vvsoft.saathi.test.util;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class StorageUtil {

    @Value("${app.record.storage.path}")
    @Getter
    private String recordStoragePath;

    @Value("${app.schema.storage.path}")
    @Getter
    private String schemaStoragePath;

    public void clearRecordStoragePath() throws IOException {
        clearDirectory(recordStoragePath);
    }

    public void clearSchemaStoragePath() throws IOException {
        clearDirectory(schemaStoragePath);
    }

    public static void clearDirectory(String storagePath) throws IOException {
        Path storage = Path.of(storagePath);
        if (Files.exists(storage))
            FileUtils.cleanDirectory(storage.toFile());
    }
}