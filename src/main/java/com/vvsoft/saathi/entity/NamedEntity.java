package com.vvsoft.saathi.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "objectInstanceType")
public abstract class NamedEntity {
    private String id;
    private String name;

    protected NamedEntity(String name) {
        this.id = "";
        this.name = name;
    }

    protected NamedEntity(){}
}
