package com.vvsoft.saathi.test.util;

import com.vvsoft.saathi.info.schema.crud.SchemaRepository;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import com.vvsoft.saathi.info.schema.model.field.FieldType;
import com.vvsoft.saathi.info.schema.model.field.SimpleField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public final class TestSchemaProvider {
    private final InfoSchema infoSchema;
    @Autowired
    public TestSchemaProvider(SchemaRepository schemaRepository,StorageUtil storageUtil) throws IOException {
        storageUtil.clearSchemaStoragePath();
        InfoSchema infoSchema = new InfoSchema("PersonalInfo",
                List.of(new SimpleField("Name", FieldType.TEXT), new SimpleField("Age", FieldType.NUMBER)));
        this.infoSchema = schemaRepository.create(infoSchema);
    }

    public InfoSchema get() {
        return infoSchema;
    }
}
