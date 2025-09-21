package com.vvsoft.saathi.drobox.filesystem;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DropboxFileSystemTest {
    private final DbxClientV2 client = new DbxClientV2(DbxRequestConfig.newBuilder("dropbox/saathi-test").build(),
            System.getenv("DROPBOX_ACCESS_TOKEN"));
    private final DropboxFileSystem dropboxFileSystem = new DropboxFileSystem(client);

    @BeforeEach
    public void setupDropbox() throws IOException {
        try {
            dropboxFileSystem.deleteFolders(Path.of("/test"));
        } catch (IOException ignore) {
        }
        dropboxFileSystem.createFolders(Path.of("/test/test1"));
    }

    @Test
    public void doesFolderExists_returns_true_if_folder_exists_relative_to_app_sandbox_path() throws IOException {
        boolean doesFolderExists = dropboxFileSystem.doesFolderExists(Path.of("/test/test1"));
        Assertions.assertTrue(doesFolderExists);
    }

    @Test
    public void doesFolderExists_returns_false_if_folder_does_not_exists() throws IOException {
        boolean doesFolderExists = dropboxFileSystem.doesFolderExists(Path.of("/test/test2"));
        Assertions.assertFalse(doesFolderExists);
    }

    @Test
    public void createFolders_creates_folder_if_folder_does_not_exists() throws IOException {
        dropboxFileSystem.createFolders(Path.of("/test/test1/testFolderCreation"));
        Assertions.assertTrue(dropboxFileSystem.doesFolderExists(Path.of("/test/test1/testFolderCreation")));
    }

    @Test
    public void createFolders_creates_multiple_folders_if_folder_does_not_exists() throws IOException {
        dropboxFileSystem.createFolders(Path.of("/test/test1/testFolderCreation/folder1/folder2"));
        Assertions.assertTrue(dropboxFileSystem.doesFolderExists(Path.of("/test/test1/testFolderCreation/folder1/folder2")));
    }

    @Test
    public void createFolders_raisesException_if_folder_already_exists() throws IOException {
        dropboxFileSystem.createFolders(Path.of("/test/test1/testFolderCreation/folder1/folder2"));
        Assertions.assertThrows(Exception.class,() ->dropboxFileSystem.createFolders(Path.of("/test/test1/testFolderCreation/folder1/folder2")));
    }

    @Test
    public void createFile_creates_file_with_given_name_and_location_with_given_content() throws IOException {
        dropboxFileSystem.createFile(Path.of("/test/testFile.txt"),"This is a test file".getBytes(StandardCharsets.UTF_8));
        Assertions.assertEquals("This is a test file",new String(dropboxFileSystem.readFile(Path.of("/test/testFile.txt"))));
    }

    @Test
    public void createFile_throws_error_if_file_already_exists() throws IOException {
        dropboxFileSystem.createFile(Path.of("/test/testDuplicateFile.txt"),"This is a test file".getBytes(StandardCharsets.UTF_8));
        Assertions.assertThrows(IOException.class,() -> dropboxFileSystem.createFile(Path.of("/test/testDuplicateFile.txt"),"This is a overwritten file".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void overwriteFile_overwrite_file_with_given_name_and_location_with_given_content() throws IOException {
        String filePath = "/test/overWriteFile.txt";
        dropboxFileSystem.createFile(Path.of(filePath),"This is a test file".getBytes(StandardCharsets.UTF_8));
        dropboxFileSystem.overwriteFile(Path.of(filePath),"This is a overwritten file".getBytes(StandardCharsets.UTF_8));
        Assertions.assertEquals("This is a overwritten file",new String(dropboxFileSystem.readFile(Path.of(filePath))));
    }

    @Test
    public void overwriteFile_throws_exception_if_file_doesnot_exists() throws IOException {
        String filePath = "/test/overWriteFile123.txt";
        Assertions.assertThrows(IOException.class,() -> dropboxFileSystem.overwriteFile(Path.of(filePath),"This is a overwritten file".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void deleteFile_deletes_file_with_given_path() throws IOException {
        String filePath = "/test/fileToDelete.txt";
        dropboxFileSystem.createFile(Path.of(filePath),"This is a test file".getBytes(StandardCharsets.UTF_8));
        dropboxFileSystem.deleteFile(Path.of(filePath));
        Assertions.assertFalse(dropboxFileSystem.doesFileExists(Path.of(filePath)));
    }

    @Test
    public void deleteFile_throws_exception_if_file_does_not_exists() throws IOException {
        String filePath = "/test/fileToDeleteRandom.txt";
        Assertions.assertThrows(IOException.class,() -> dropboxFileSystem.deleteFile(Path.of(filePath)));
    }

    @Test
    public void walk_returns_all_objects_at_folder_location() throws IOException {
        Path walkPath = Path.of("/test/walk/");
        dropboxFileSystem.createFolders(walkPath);
        dropboxFileSystem.createFile(walkPath.resolve("file1.txt"),"file1.txt".getBytes(StandardCharsets.UTF_8));
        dropboxFileSystem.createFile(walkPath.resolve("file2.txt"),"file2.txt".getBytes(StandardCharsets.UTF_8));
        Stream<Path> paths = dropboxFileSystem.walk(walkPath);
        List<Path> pathsList = paths.collect(Collectors.toList());
        Assertions.assertEquals(2,pathsList.size());
        Assertions.assertTrue(pathsList.stream().anyMatch(p -> p.getFileName().toString().equals("file1.txt")));
        Assertions.assertTrue(pathsList.stream().anyMatch(p -> p.getFileName().toString().equals("file2.txt")));
    }


}