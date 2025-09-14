package com.vvsoft.saathi.drobox;

import java.nio.file.Path;

public interface DropboxClient {
    void clearFolder(Path location);
    void uploadFile(Path uploadPath, String fileName, byte[] fileContent);
}
