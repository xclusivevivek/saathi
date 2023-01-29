package com.vvsoft.saathi;

import com.vvsoft.saathi.info.schema.model.Copyable;

public interface NamedEntity extends Copyable<NamedEntity> {
    String getId();
    void setId(String id);
    String getName();
}
