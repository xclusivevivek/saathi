package com.vvsoft.saathi.info.schema.model;

import com.vvsoft.saathi.NamedEntity;
import lombok.Data;

@Data
public abstract class InfoSchema implements NamedEntity {
    private String id;
    private String name;

    protected InfoSchema(String name) {
        this.id = "";
        this.name = name;
    }
}
