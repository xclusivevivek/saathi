package com.vvsoft.saathi.entity;

import lombok.Data;

@Data
public abstract class NamedEntity {
    private String id;
    private String name;

    protected NamedEntity(String name) {
        this.id = "";
        this.name = name;
    }
}
