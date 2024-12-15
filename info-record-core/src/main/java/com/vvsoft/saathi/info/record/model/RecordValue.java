package com.vvsoft.saathi.info.record.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vvsoft.saathi.info.schema.model.Copyable;
import com.vvsoft.saathi.info.schema.model.InfoSchema;

import java.util.Map;
import java.util.Optional;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "objectInstanceType")
public interface RecordValue extends Copyable<RecordValue> {
    Optional<Object> getValue(String field);
    void update(String field,String value) throws NoSuchFieldException;
    InfoSchema getInfoSchema();
    Map<String,Object> getValues();
}
