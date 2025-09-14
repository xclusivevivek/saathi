package com.vvsoft.saathi.drobox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MockDropboxClient implements DropboxClient {

    @Override
    public void clearFolder(Path location) {
        try {
            Files.walk(location,1).forEach( p ->
            {
                try {
                    if(!Files.isDirectory(p))
                        Files.delete(p);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadFile(Path uploadPath, String fileName, byte[] fileContent) {

    }
}
