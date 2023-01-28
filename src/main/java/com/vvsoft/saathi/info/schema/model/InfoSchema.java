package com.vvsoft.saathi.info.schema.model;

import lombok.Data;

@Data
public abstract class InfoSchema implements Copyable<InfoSchema>{
    private String id;
    private String name;

    protected InfoSchema(String name) {
        this.id = "";
        this.name = name;
    }
}
