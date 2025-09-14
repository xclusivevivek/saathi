package com.vvsoft.saathi.entity.persistance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class LocalDiskStorageFileSystem implements StorageFileSystem{
    @Override
    public void createFolders(Path folderPath) throws IOException {
        Files.createDirectories(folderPath);
    }

    @Override
    public Stream<Path> walk(Path folder) throws IOException {
        return Files.walk(folder, 2);
    }

    @Override
    public void createFile(Path createdFile, byte[] content) throws IOException {
        Files.write(createdFile, content, StandardOpenOption.CREATE);
    }

    @Override
    public void overwriteFile(Path filePath, byte[] content) throws IOException {
        Files.write(filePath, content,StandardOpenOption.WRITE);
    }

    @Override
    public void deleteFile(Path deleteFilePath) throws IOException {
        Files.delete(deleteFilePath);
    }

    @Override
    public boolean doesFolderExists(Path folderPath) {
        return Files.exists(folderPath);
    }
}
