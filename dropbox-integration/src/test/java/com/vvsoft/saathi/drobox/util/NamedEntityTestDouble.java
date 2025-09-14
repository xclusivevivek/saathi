package com.vvsoft.saathi.drobox.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vvsoft.saathi.entity.NamedEntity;
import com.vvsoft.saathi.info.schema.model.Copyable;
import lombok.Getter;
import lombok.Setter;

public class NamedEntityTestDouble extends NamedEntity implements Copyable<NamedEntityTestDouble> {
    @Getter
    @Setter
    private String data;

    @JsonCreator
    public NamedEntityTestDouble(@JsonProperty("name") String name) {
        super(name);
    }

    public NamedEntityTestDouble(String name, String data) {
        this(name);
        this.data = data;
    }

    @Override
    public NamedEntityTestDouble copy() {
        NamedEntityTestDouble entity = new NamedEntityTestDouble(this.getName(), this.getData());
        entity.setId(this.getId());
        return entity;
    }
}
