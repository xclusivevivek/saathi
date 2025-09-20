package com.vvsoft.saathi.drobox.filesystem;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DropboxFileSystemTest {
    private final DbxClientV2 client = new DbxClientV2(DbxRequestConfig.newBuilder("dropbox/saathi-test").build(),
            "");
    private final DropboxFileSystem dropboxFileSystem = new DropboxFileSystem(client);

    @Test
    public void doesFolderExists_returns_true_if_folder_exists_relative_to_app_sandbox_path() throws IOException {
        boolean doesFolderExists = dropboxFileSystem.doesFolderExists(Path.of("/test/test1"));
        Assertions.assertTrue(doesFolderExists);
    }
}