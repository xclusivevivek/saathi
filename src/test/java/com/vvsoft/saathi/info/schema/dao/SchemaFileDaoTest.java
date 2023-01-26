package com.vvsoft.saathi.info.schema.dao;

import com.vvsoft.saathi.info.schema.model.SimpleSchema;
import com.vvsoft.saathi.info.schema.model.field.FieldType;
import com.vvsoft.saathi.info.schema.model.field.SimpleField;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class SchemaFileDaoTest {

    public static final String STORAGE_PATH = "data/test/schema/";

    @BeforeEach
    public void setup() throws IOException {
        Path storage = Path.of(STORAGE_PATH);
        FileUtils.cleanDirectory(storage.toFile());
    }

    @Test
    void onCreateFileShouldBeCreatedForSchema() throws IOException {
        SchemaFileDao<SimpleSchema> fileDao = new SchemaFileDao<>(STORAGE_PATH);
        fileDao.create(new SimpleSchema(1L,"Personal Info",
                List.of(new SimpleField("Name", FieldType.TEXT),new SimpleField("Age",FieldType.NUMBER))));
        Assertions.assertTrue(Files.exists(Path.of(STORAGE_PATH).resolve("1.schema")));
    }
}