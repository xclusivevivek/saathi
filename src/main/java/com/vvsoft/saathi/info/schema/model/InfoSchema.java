package com.vvsoft.saathi.info.schema.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vvsoft.saathi.entity.NamedEntity;
import com.vvsoft.saathi.info.schema.model.field.SimpleField;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class InfoSchema extends NamedEntity implements Copyable<InfoSchema>{
    private final Set<SimpleField> fields = new HashSet<>();
    @JsonCreator
    public InfoSchema(@JsonProperty("name") String name, @JsonProperty("fields") Collection<SimpleField> fields) {
        super(name);
        this.fields.addAll(fields);
    }

    public Collection<SimpleField> getFields() {
        return Collections.unmodifiableCollection(fields);
    }

    public void add(SimpleField job) {
        fields.add(job);
    }


    @Override
    public InfoSchema copy() {
        InfoSchema infoSchema = new InfoSchema(this.getName(), new HashSet<>(fields));
        infoSchema.setId(this.getId());
        return infoSchema;
    }
}
