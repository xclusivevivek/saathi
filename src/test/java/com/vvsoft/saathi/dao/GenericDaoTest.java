package com.vvsoft.saathi.dao;

import com.vvsoft.saathi.NamedEntity;
import com.vvsoft.saathi.dao.exception.EntityAlreadyExistsException;
import com.vvsoft.saathi.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.info.schema.model.Copyable;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

class GenericDaoTest {

    public static final String STORAGE_PATH = "data/test/entity/";
    private GenericDao<NamedEntityTestDouble> genericDao;

    @BeforeEach
    public void setup() throws IOException {
        Path storage = Path.of(STORAGE_PATH);
        if(Files.exists(storage))
            FileUtils.cleanDirectory(storage.toFile());
        genericDao = new GenericLocalStorageDao<>(STORAGE_PATH, "entity");
    }

    @Test
    void schemaShouldBeCreatedWithIdGenerated() throws EntityAlreadyExistsException {
        NamedEntityTestDouble namedEntityTestDouble = new NamedEntityTestDouble("TestSchema");
        NamedEntityTestDouble entity = genericDao.create(namedEntityTestDouble);
        Assertions.assertNotEquals("", entity.getId());
    }

    @Test
    void duplicateSchemaShouldNotBeCreated() throws EntityAlreadyExistsException {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestSchema", "foo");
        NamedEntityTestDouble entity2 = new NamedEntityTestDouble("TestSchema", "bar");
        NamedEntityTestDouble entity = genericDao.create(entity1);
        Assertions.assertNotEquals("", entity.getId());
        Assertions.assertThrows(EntityAlreadyExistsException.class,() -> genericDao.create(entity2));
    }

    @Test
    void canGetExistingSchemaById() throws EntityAlreadyExistsException {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestSchema", "foo");
        genericDao.create(entity1);
        String savedId = entity1.getId();

        Optional<NamedEntityTestDouble> retrievedSchema = genericDao.read(savedId);
        Assertions.assertTrue(retrievedSchema.isPresent());
        Assertions.assertEquals("TestSchema",retrievedSchema.get().getName());
    }

    @Test
    void canReadExistingSchemaById() throws EntityAlreadyExistsException {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestSchema", "foo");
        genericDao.create(entity1);
        String savedId = entity1.getId();

        Optional<NamedEntityTestDouble> retrievedSchema = genericDao.read(savedId);
        Assertions.assertTrue(retrievedSchema.isPresent());
        Assertions.assertEquals("TestSchema",retrievedSchema.get().getName());
    }

    @Test
    void cannotReadNonExistingSchemaById() throws EntityAlreadyExistsException {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestSchema", "foo");
        genericDao.create(entity1);
        String savedId = entity1.getId();

        Optional<NamedEntityTestDouble> retrievedSchema = genericDao.read(savedId + "1");
        Assertions.assertTrue(retrievedSchema.isEmpty());
    }

    @Test
    void canUpdateSchema() throws EntityAlreadyExistsException {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestSchema", "foo");
        genericDao.create(entity1);
        entity1.setData("bar");
        genericDao.update(entity1);
        Optional<NamedEntityTestDouble> readEntity = genericDao.read(entity1.getId());
        Assertions.assertTrue(readEntity.isPresent());
        Assertions.assertEquals("bar",readEntity.get().getData());

    }

    @Test
    void cantUpdateSchemaIfItDoesNotExists() {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestSchema", "foo");
        Assertions.assertThrows(EntityNotFoundException.class,() -> genericDao.update(entity1));
    }

    @Test
    void cantDeleteSchemaIfItDoesNotExists() {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestSchema", "foo");
        String id = entity1.getId();
        Assertions.assertThrows(EntityNotFoundException.class,() -> genericDao.delete(id));
    }

    @Test
    void canDeleteSchemaIfItExists() throws EntityAlreadyExistsException {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestSchema", "foo");
        genericDao.create(entity1);
        genericDao.delete(entity1.getId());
        Optional<NamedEntityTestDouble> schema = genericDao.read(entity1.getId());
        Assertions.assertTrue(schema.isEmpty());
    }


    public static class NamedEntityTestDouble extends NamedEntity implements Copyable<NamedEntityTestDouble>{
        @Getter
        @Setter
        private String data;
    
        public NamedEntityTestDouble(String name) {
            super(name);
        }

        public NamedEntityTestDouble(String name,String data) {
            this(name);
            this.data = data;
        }

        @Override
        public NamedEntityTestDouble copy() {
            NamedEntityTestDouble entity = new NamedEntityTestDouble(this.getName(), this.getData());
            entity.setId(this.getId());
            return entity;
        }
    }

}