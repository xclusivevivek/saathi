package com.vvsoft.saathi.info.schema;

import com.vvsoft.saathi.entity.dao.GenericLocalStorageDao;
import com.vvsoft.saathi.entity.dao.exception.EntityAlreadyExistsException;
import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.info.schema.dto.InfoSchemaDto;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import com.vvsoft.saathi.info.schema.model.field.FieldType;
import com.vvsoft.saathi.info.schema.model.field.SimpleField;
import com.vvsoft.saathi.test.util.StorageUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class SchemaServiceImplTest {
    private final String path = "data/test/" + this.getClass().getName();

    private SchemaRepository schemaRepository;

    @BeforeEach
    void setupTest() throws IOException {
        schemaRepository = new SchemaServiceImpl(new GenericLocalStorageDao<>(path,"schema"));
    }

    @AfterEach
    void cleanTest() throws IOException {
        StorageUtil.clearDirectory(path);
    }

    @Test
    void canCreateNewSchema(){
        InfoSchemaDto dto = setupDto("address");
        InfoSchema infoSchema = schemaRepository.create(dto.toSchema());
        Assertions.assertNotEquals("", infoSchema.getId());
    }

    @Test
    void cannotCreateSameSchemaAgain(){
        InfoSchemaDto dto = setupDto("address");
        InfoSchema schema = dto.toSchema();
        schemaRepository.create(schema);
        Assertions.assertThrows(EntityAlreadyExistsException.class,() -> schemaRepository.create(schema));
    }

    @Test
    void canFindCreatedSchemaAgain(){
        InfoSchemaDto dto = setupDto("address");
        InfoSchema infoSchema1 = schemaRepository.create(dto.toSchema());
        Optional<InfoSchema> search = schemaRepository.find(infoSchema1.getName());
        Assertions.assertTrue(search.isPresent());
        Assertions.assertEquals(infoSchema1.getId(), search.get().getId());
    }

    @Test
    void cannotFindMissingSchema(){
        Assertions.assertTrue(schemaRepository.find("foo").isEmpty());
    }

    @Test
    void canUpdateSchema(){
        InfoSchemaDto foo = setupDto("foo");
        InfoSchema infoSchema = schemaRepository.create(foo.toSchema());
        SimpleField newField = new SimpleField("pin", FieldType.NUMBER);
        infoSchema.add(newField);
        InfoSchema updatedSchema = schemaRepository.update(infoSchema);
        Assertions.assertTrue(updatedSchema.getFields().contains(newField));
    }

    @Test
    void updateMissingSchemaProduceError(){
        InfoSchemaDto foo = setupDto("foo");
        InfoSchema infoSchema = foo.toSchema();
        SimpleField newField = new SimpleField("pin", FieldType.NUMBER);
        infoSchema.add(newField);
        Assertions.assertThrows(EntityNotFoundException.class,() -> schemaRepository.update(infoSchema));
    }

    @Test
    void canDeleteSchema(){
        InfoSchemaDto foo = setupDto("foo");
        InfoSchema infoSchema = schemaRepository.create(foo.toSchema());
        schemaRepository.delete(infoSchema.getName());
        Assertions.assertTrue(schemaRepository.find(infoSchema.getName()).isEmpty());
    }

    @Test
    void canotDeleteMissingSchema(){
        InfoSchemaDto foo = setupDto("foo");
        String infoSchemaName = foo.toSchema().getName();
        Assertions.assertThrows(EntityNotFoundException.class,() -> schemaRepository.delete(infoSchemaName));
    }

    @Test
    void canFindAllSchemas(){
        InfoSchemaDto foo = setupDto("foo");
        InfoSchemaDto bar = setupDto("bar");
        schemaRepository.create(foo.toSchema());
        schemaRepository.create(bar.toSchema());
        List<InfoSchema> all = schemaRepository.findAll();
        List<String> names = all.stream().map(InfoSchema::getName).collect(Collectors.toList());
        Assertions.assertTrue(names.contains("foo"));
        Assertions.assertTrue(names.contains("bar"));
    }

    private static InfoSchemaDto setupDto(String name) {
        return InfoSchemaDto.builder().name(name)
                .field(new SimpleField("flatNo", FieldType.NUMBER))
                .field(new SimpleField("Street", FieldType.TEXT))
                .build();
    }


}