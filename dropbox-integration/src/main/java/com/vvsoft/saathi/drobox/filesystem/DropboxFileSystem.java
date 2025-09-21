package com.vvsoft.saathi.drobox.filesystem;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.vvsoft.saathi.entity.persistance.StorageFileSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DropboxFileSystem implements StorageFileSystem {

    private final DbxClientV2 client;

    public DropboxFileSystem(DbxClientV2 client) {
        this.client = client;
    }


    @Override
    public void createFolders(Path folderPath) throws IOException {
        try {
            client.files().createFolderV2(folderPath.toString());
        } catch (DbxException e) {
            throw new IOException("Error in creating folder " + folderPath,e);
        }
    }

    @Override
    public Stream<Path> walk(Path folder) throws IOException {
        List<Path> files = new ArrayList<>();
        try {
            ListFolderResult listFolderResult = client.files().listFolder(folder.toString());
            List<Metadata> entries = listFolderResult.getEntries();
            if(!entries.isEmpty()) {
                Path tempDirectory = Files.createTempDirectory("saathiTemp");
                entries.forEach( m -> {
                    Path sourcePath = Path.of(m.getPathDisplay());
                    Path destPath = tempDirectory.resolve(m.getName());
                    byte[] content = readFile(sourcePath);
                    try {
                        Files.write(destPath,content);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    files.add(destPath);
                });
            }
        } catch (DbxException e) {
            throw new IOException("Error while walking the path " + folder,e);
        }
        return files.stream();
    }

    @Override
    public void createFile(Path createdFile, byte[] content) throws IOException {
        UploadBuilder uploadBuilder = client.files().uploadBuilder(createdFile.toString());
        uploadBuilder.withMode(WriteMode.ADD);
        try {
            FileMetadata fileMetadata = uploadBuilder.uploadAndFinish(new ByteArrayInputStream(content));
        } catch (DbxException e) {
            throw new IOException("Unable to create file " + createdFile,e);
        }
    }


    @Override
    public void overwriteFile(Path filePath, byte[] content) throws IOException {
        try {
            Metadata fileMetadata = client.files().getMetadata(filePath.toString());
            if(!fileMetadata.getName().equals(filePath.getFileName().toString()))
                throw new IOException("File does not exists : " + filePath.toString());
            UploadBuilder uploadBuilder = client.files().uploadBuilder(filePath.toString());
            uploadBuilder.withMode(WriteMode.OVERWRITE);
            uploadBuilder.uploadAndFinish(new ByteArrayInputStream(content));
        } catch (DbxException e) {
            throw new IOException("Unable to create file " + filePath,e);
        }
    }

    @Override
    public void deleteFile(Path deleteFilePath) throws IOException {
        try {
            client.files().deleteV2(deleteFilePath.toString());
        } catch (DbxException e) {
            throw new IOException("Error in deleting file " + deleteFilePath,e);
        }
    }

    @Override
    public boolean doesFolderExists(Path folderPath) throws IOException {
        Metadata metadata = null;
        try {
            metadata = client.files().getMetadata(folderPath.toString());
        } catch (DbxException e) {
            return false;
        }

        return metadata.getName().equals(folderPath.getFileName().toString());
    }

    public byte[] readFile(Path filePath) {
        try {
            DbxDownloader<FileMetadata> downloader = client.files().download(filePath.toString());
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            FileMetadata result = downloader.download(outStream);
            return outStream.toByteArray();
        } catch (DbxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean doesFileExists(Path filePath) {
        Metadata fileMetadata = null;
        try {
            fileMetadata = client.files().getMetadata(filePath.toString());
            return fileMetadata.getName().equals(filePath.getFileName().toString());
        } catch (DbxException e) {
            return false;
        }
    }

    public void deleteFolders(Path folder) throws IOException {
        try {
            client.files().deleteV2(folder.toString());
        } catch (DbxException e) {
            throw new IOException("Could not delete folder " + folder,e);
        }
    }
}
