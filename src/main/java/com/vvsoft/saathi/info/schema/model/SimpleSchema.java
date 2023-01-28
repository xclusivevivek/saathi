package com.vvsoft.saathi.info.schema.model;

import com.vvsoft.saathi.info.schema.model.field.SimpleField;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SimpleSchema extends InfoSchema {
    private final Set<SimpleField> fields = new HashSet<>();
    public SimpleSchema(String name, Collection<SimpleField> fields) {
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
    public SimpleSchema copy() {
        SimpleSchema simpleSchema = new SimpleSchema(this.getName(), new HashSet<>(fields));
        simpleSchema.setId(this.getId());
        return simpleSchema;
    }
}
