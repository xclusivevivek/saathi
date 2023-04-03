package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.info.schema.model.Copyable;

import java.util.Optional;

public interface RecordValue extends Copyable<RecordValue> {
    Optional<Object> getValue(String field);
    void update(String field,String value) throws NoSuchFieldException;
}
