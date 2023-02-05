package com.vvsoft.saathi.test.util;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StorageUtil {
    public static void clearDirectory(String storagePath) throws IOException {
        Path storage = Path.of(storagePath);
        if (Files.exists(storage))
            FileUtils.cleanDirectory(storage.toFile());
    }
}