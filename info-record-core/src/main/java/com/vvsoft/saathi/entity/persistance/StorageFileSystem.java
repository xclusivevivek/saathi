package com.vvsoft.saathi.entity.persistance;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageFileSystem {
    void createFolders(Path folderPath) throws IOException;
    Stream<Path> walk(Path folder) throws IOException;
    void createFile(Path createdFile, byte[] content) throws IOException;
    void overwriteFile(Path filePath, byte[] content) throws IOException;
    void deleteFile(Path deleteFilePath) throws IOException;
    boolean doesFolderExists(Path folderPath) throws IOException;
}
