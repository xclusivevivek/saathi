package com.vvsoft.saathi.info.schema.dao;

import com.vvsoft.saathi.dao.GenericLocalStorageDao;
import com.vvsoft.saathi.dao.exception.EntityAlreadyExistsException;
import com.vvsoft.saathi.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import com.vvsoft.saathi.info.schema.model.SimpleSchema;
import com.vvsoft.saathi.info.schema.model.field.FieldType;
import com.vvsoft.saathi.info.schema.model.field.SimpleField;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

class InfoSchemaDaoTest {

    public static final String STORAGE_PATH = "data/test/schema/";
    private InfoSchemaDaoImpl<InfoSchema> dao;
    private InfoSchemaDaoImpl<SimpleSchema> simpleDao;

    @BeforeEach
    public void setup() throws IOException {
        Path storage = Path.of(STORAGE_PATH);
        FileUtils.cleanDirectory(storage.toFile());
        dao = new InfoSchemaDaoImpl<>(new GenericLocalStorageDao<>(STORAGE_PATH,"schema"));
        simpleDao = new InfoSchemaDaoImpl<>(new GenericLocalStorageDao<>(STORAGE_PATH,"schema"));
    }

    @Test
    void schemaShouldBeCreatedWithIdGenerated() throws EntityAlreadyExistsException {
        SimpleSchema simpleSchema = new SimpleSchema("TestScema", List.of(new SimpleField("foo", FieldType.TEXT), new SimpleField("bar", FieldType.NUMBER)));
        InfoSchema infoSchema = dao.create(simpleSchema);
        Assertions.assertNotEquals("", infoSchema.getId());
    }

    @Test
    void duplicateSchemaShouldNotBeCreated() throws EntityAlreadyExistsException {
        SimpleSchema simpleSchema1 = new SimpleSchema("TestSchema", List.of(new SimpleField("foo", FieldType.TEXT),
                new SimpleField("bar", FieldType.NUMBER)));
        SimpleSchema simpleSchema2 = new SimpleSchema("TestSchema", List.of(new SimpleField("xyz", FieldType.TEXT),
                new SimpleField("spv", FieldType.NUMBER)));
        InfoSchema infoSchema = dao.create(simpleSchema1);
        Assertions.assertNotEquals("", infoSchema.getId());
        Assertions.assertThrows(EntityAlreadyExistsException.class,() -> dao.create(simpleSchema2));
    }

    @Test
    void canGetExistingSchemaById() throws EntityAlreadyExistsException {
        SimpleSchema simpleSchema1 = new SimpleSchema("TestSchema", List.of(new SimpleField("foo", FieldType.TEXT),
                new SimpleField("bar", FieldType.NUMBER)));
        dao.create(simpleSchema1);
        String savedId = simpleSchema1.getId();

        Optional<InfoSchema> retrievedSchema = dao.read(savedId);
        Assertions.assertTrue(retrievedSchema.isPresent());
        Assertions.assertEquals("TestSchema",retrievedSchema.get().getName());
    }

    @Test
    void canReadExistingSchemaById() throws EntityAlreadyExistsException {
        SimpleSchema simpleSchema1 = new SimpleSchema("TestSchema", List.of(new SimpleField("foo", FieldType.TEXT),
                new SimpleField("bar", FieldType.NUMBER)));
        dao.create(simpleSchema1);
        String savedId = simpleSchema1.getId();

        Optional<InfoSchema> retrievedSchema = dao.read(savedId);
        Assertions.assertTrue(retrievedSchema.isPresent());
        Assertions.assertEquals("TestSchema",retrievedSchema.get().getName());
    }

    @Test
    void cannotReadNonExistingSchemaById() throws EntityAlreadyExistsException {
        SimpleSchema simpleSchema1 = new SimpleSchema("TestSchema", List.of(new SimpleField("foo", FieldType.TEXT),
                new SimpleField("bar", FieldType.NUMBER)));
        dao.create(simpleSchema1);
        String savedId = simpleSchema1.getId();

        Optional<InfoSchema> retrievedSchema = dao.read(savedId + "1");
        Assertions.assertTrue(retrievedSchema.isEmpty());
    }

    @Test
    void canUpdateSchema() throws EntityAlreadyExistsException {
        SimpleSchema simpleSchema1 = new SimpleSchema("TestSchema", List.of(new SimpleField("foo", FieldType.TEXT),
                new SimpleField("bar", FieldType.NUMBER)));
        simpleDao.create(simpleSchema1);
        SimpleField newField = new SimpleField("xyz", FieldType.TEXT);
        simpleSchema1.add(newField);
        simpleDao.update(simpleSchema1);
        Optional<SimpleSchema> readSchema = simpleDao.read(simpleSchema1.getId());
        Assertions.assertTrue(readSchema.isPresent());
        Assertions.assertTrue(readSchema.get().getFields().contains(newField));

    }

    @Test
    void cantUpdateSchemaIfItDoesNotExists() {
        SimpleSchema simpleSchema1 = new SimpleSchema("TestSchema", List.of(new SimpleField("foo", FieldType.TEXT),
                new SimpleField("bar", FieldType.NUMBER)));
        Assertions.assertThrows(EntityNotFoundException.class,() -> simpleDao.update(simpleSchema1));
    }

    @Test
    void cantDeleteSchemaIfItDoesNotExists() {
        SimpleSchema simpleSchema1 = new SimpleSchema("TestSchema", List.of(new SimpleField("foo", FieldType.TEXT),
                new SimpleField("bar", FieldType.NUMBER)));
        String id = simpleSchema1.getId();
        Assertions.assertThrows(EntityNotFoundException.class,() -> simpleDao.delete(id));
    }

    @Test
    void canDeleteSchemaIfItExists() throws EntityAlreadyExistsException {
        SimpleSchema simpleSchema1 = new SimpleSchema("TestSchema", List.of(new SimpleField("foo", FieldType.TEXT),
                new SimpleField("bar", FieldType.NUMBER)));
        simpleDao.create(simpleSchema1);
        simpleDao.delete(simpleSchema1.getId());
        Optional<SimpleSchema> schema = simpleDao.read(simpleSchema1.getId());
        Assertions.assertTrue(schema.isEmpty());
    }




}