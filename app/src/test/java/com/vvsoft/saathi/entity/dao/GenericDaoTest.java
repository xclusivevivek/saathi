package com.vvsoft.saathi.entity.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vvsoft.saathi.entity.NamedEntity;
import com.vvsoft.saathi.entity.dao.exception.EntityAlreadyExistsException;
import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.info.GenericLocalStorageDao;
import com.vvsoft.saathi.info.schema.model.Copyable;
import com.vvsoft.saathi.test.util.StorageUtil;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

class GenericDaoTest {

    public static final String STORAGE_PATH = "data/test/entity/";
    private GenericDao<NamedEntityTestDouble> genericDao;

    @BeforeEach
    public void setup() throws IOException {
        genericDao = new GenericLocalStorageDao<>(STORAGE_PATH, "entity");
    }

    @AfterEach
    public void cleanup() throws IOException {
        StorageUtil.clearDirectory(STORAGE_PATH);
    }

    @Test
    void schemaShouldBeCreatedWithIdGenerated() throws EntityAlreadyExistsException {
        NamedEntityTestDouble namedEntityTestDouble = new NamedEntityTestDouble("TestEntity");
        NamedEntityTestDouble entity = genericDao.create(namedEntityTestDouble);
        Assertions.assertNotEquals("", entity.getId());
    }

    @Test
    void duplicateEntityShouldNotBeCreated() throws EntityAlreadyExistsException {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestEntity", "foo");
        NamedEntityTestDouble entity2 = new NamedEntityTestDouble("TestEntity", "bar");
        NamedEntityTestDouble entity = genericDao.create(entity1);
        Assertions.assertNotEquals("", entity.getId());
        Assertions.assertThrows(EntityAlreadyExistsException.class,() -> genericDao.create(entity2));
    }

    @Test
    void canGetExistingEntityByName() throws EntityAlreadyExistsException {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestEntity", "foo");
        genericDao.create(entity1);
        String savedName = entity1.getName();

        Optional<NamedEntityTestDouble> retrievedEntity = genericDao.read(savedName);
        Assertions.assertTrue(retrievedEntity.isPresent());
        Assertions.assertEquals("TestEntity",retrievedEntity.get().getName());
    }

    @Test
    void canReadExistingEntityByName() throws EntityAlreadyExistsException {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestEntity", "foo");
        genericDao.create(entity1);
        String savedName = entity1.getName();

        Optional<NamedEntityTestDouble> retrievedEntity = genericDao.read(savedName);
        Assertions.assertTrue(retrievedEntity.isPresent());
        Assertions.assertEquals("TestEntity",retrievedEntity.get().getName());
    }

    @Test
    void cannotReadNonExistingEntityById() throws EntityAlreadyExistsException {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestEntity", "foo");
        genericDao.create(entity1);
        String savedId = entity1.getId();

        Optional<NamedEntityTestDouble> retrievedEntity = genericDao.read(savedId + "1");
        Assertions.assertTrue(retrievedEntity.isEmpty());
    }

    @Test
    void canUpdateEntity() throws EntityAlreadyExistsException {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestEntity", "foo");
        genericDao.create(entity1);
        entity1.setData("bar");
        genericDao.update(entity1);
        Optional<NamedEntityTestDouble> readEntity = genericDao.read(entity1.getName());
        Assertions.assertTrue(readEntity.isPresent());
        Assertions.assertEquals("bar",readEntity.get().getData());

    }

    @Test
    void cantUpdateEntityIfItDoesNotExists() {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestEntity", "foo");
        Assertions.assertThrows(EntityNotFoundException.class,() -> genericDao.update(entity1));
    }

    @Test
    void cantDeleteEntityIfItDoesNotExists() {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestEntity", "foo");
        String id = entity1.getId();
        Assertions.assertThrows(EntityNotFoundException.class,() -> genericDao.delete(id));
    }

    @Test
    void canDeleteEntityIfItExists() throws EntityAlreadyExistsException {
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("TestEntity", "foo");
        genericDao.create(entity1);
        genericDao.delete(entity1.getName());
        Optional<NamedEntityTestDouble> schema = genericDao.read(entity1.getId());
        Assertions.assertTrue(schema.isEmpty());
    }
    
    @Test
    void canListAllEntity(){
        NamedEntityTestDouble entity1 = new NamedEntityTestDouble("FooEntity", "foo");
        NamedEntityTestDouble entity2 = new NamedEntityTestDouble("BarEntity", "bar");
        genericDao.create(entity1);
        genericDao.create(entity2);

        List<NamedEntityTestDouble> entities = genericDao.getAll();
        Assertions.assertTrue(entities.contains(entity1));
        Assertions.assertTrue(entities.contains(entity2));
    }

    public static class NamedEntityTestDouble extends NamedEntity implements Copyable<NamedEntityTestDouble>{
        @Getter
        @Setter
        private String data;

        @JsonCreator
        public NamedEntityTestDouble(@JsonProperty("name") String name) {
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