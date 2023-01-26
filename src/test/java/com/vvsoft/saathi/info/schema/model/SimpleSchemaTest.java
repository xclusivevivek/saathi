package com.vvsoft.saathi.info.schema.model;

import com.vvsoft.saathi.info.schema.model.field.FieldType;
import com.vvsoft.saathi.info.schema.model.field.SimpleField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class SimpleSchemaTest {
    @Test
    void createdSchemaHaveIdAndName(){
        List<SimpleField> simpleFields = createFields();
        SimpleSchema personalInfo = new SimpleSchema(1L, "Personal Info", simpleFields);
        Assertions.assertEquals("Personal Info",personalInfo.getName());
        Assertions.assertEquals(1L,personalInfo.getId());
    }

    @Test
    void canListSchemaFields(){
        List<SimpleField> simpleFields = createFields();
        SimpleSchema personalInfo = new SimpleSchema(1L, "Personal Info", simpleFields);
        Collection<SimpleField> fields = personalInfo.getFields();
        Assertions.assertTrue(fields.containsAll(simpleFields));
    }

    @Test
    void checkCannotModifyFields(){
        List<SimpleField> simpleFields = createFields();
        SimpleSchema personalInfo = new SimpleSchema(1L, "Personal Info", simpleFields);
        Collection<SimpleField> fields = personalInfo.getFields();
        SimpleField job = new SimpleField("job", FieldType.TEXT);
        Assertions.assertThrows(UnsupportedOperationException.class,() -> fields.add(job));
    }

    @Test
    void checkCanAddFields(){
        List<SimpleField> simpleFields = createFields();
        SimpleSchema personalInfo = new SimpleSchema(1L, "Personal Info", simpleFields);
        SimpleField job = new SimpleField("job", FieldType.TEXT);
        personalInfo.add(job);
        Assertions.assertTrue(personalInfo.getFields().contains(job));
    }

    private static List<SimpleField> createFields() {
        List<SimpleField> simpleFields = new ArrayList<>();
        simpleFields.add(new SimpleField("name", FieldType.TEXT));
        simpleFields.add(new SimpleField("age", FieldType.NUMBER));
        return simpleFields;
    }


}
