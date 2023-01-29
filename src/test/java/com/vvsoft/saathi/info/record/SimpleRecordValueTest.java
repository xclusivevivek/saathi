package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.info.schema.model.SimpleSchema;
import com.vvsoft.saathi.info.schema.model.field.FieldType;
import com.vvsoft.saathi.info.schema.model.field.SimpleField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleRecordValueTest {

    @Test
    void recordShouldBeCreatedWithGivenValuesFromSchema() throws NoSuchFieldException {
        SimpleSchema schema = new SimpleSchema("Address", List.of(new SimpleField("House No.", FieldType.NUMBER),
                new SimpleField("Street", FieldType.TEXT), new SimpleField("Area", FieldType.AMOUNT)));
        SimpleRecordValue recordValue = SimpleRecordValue.builder(schema)
                                .addValue("House No.","125")
                                .addValue("Street","Any streeet")
                                .addValue("Area","500.6")
                                .build();
        Assertions.assertAll( () -> Assertions.assertTrue(recordValue.getValues().containsValue(125L)),
                () -> assertTrue(recordValue.getValues().containsValue(500.6d)),
                () -> assertTrue(recordValue.getValues().containsValue("Any streeet")),
                () -> assertEquals(3,recordValue.getValues().size()));
    }

    @Test
    void recordShouldNotBeCreatedForFieldNotInSchema(){
        SimpleSchema schema = new SimpleSchema("Address", List.of(new SimpleField("House No.", FieldType.NUMBER),
                new SimpleField("Street", FieldType.TEXT), new SimpleField("Area", FieldType.AMOUNT)));
        Assertions.assertThrows(NoSuchFieldException.class,() -> SimpleRecordValue.builder(schema)
                .addValue("Area_new","500.6")
                .build());
    }

    @Test
    void recordShouldBeCreatedWithDefaultValueForMissingFields() throws NoSuchFieldException {
        SimpleSchema schema = new SimpleSchema("Address", List.of(new SimpleField("House No.", FieldType.NUMBER),
                new SimpleField("Street", FieldType.TEXT), new SimpleField("Area", FieldType.AMOUNT)));
        SimpleRecordValue recordValue = SimpleRecordValue.builder(schema)
                .addValue("House No.","125")
                .build();
        Assertions.assertAll(
                () -> assertEquals(125L, recordValue.getValues().get("House No.")),
                () -> assertEquals("", recordValue.getValues().get("Street")),
                () -> assertEquals(0.0, recordValue.getValues().get("Area")),
                () -> assertEquals(3,recordValue.getValues().size()));
    }
}