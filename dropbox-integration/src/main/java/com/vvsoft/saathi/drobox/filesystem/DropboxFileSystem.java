package com.vvsoft.saathi.drobox.filesystem;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.common.PathRoot;
import com.dropbox.core.v2.files.Metadata;
import com.vvsoft.saathi.entity.persistance.StorageFileSystem;


import java.io.IOException;
import java.nio.file.Path;
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
        return Stream.empty();
    }

    @Override
    public void createFile(Path createdFile, byte[] content) throws IOException {

    }

    @Override
    public void overwriteFile(Path filePath, byte[] content) throws IOException {

    }

    @Override
    public void deleteFile(Path deleteFilePath) throws IOException {

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
}
