package com.vvsoft.saathi.info.schema;

import com.vvsoft.saathi.info.schema.field.SimpleField;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SimpleSchema extends AbstractInfoSchema{
    private final Set<SimpleField> fields = new HashSet<>();
    public SimpleSchema(long id, String name, Collection<SimpleField> fields) {
        super(id, name);
        this.fields.addAll(fields);
    }

    public Collection<SimpleField> getFields() {
        return Collections.unmodifiableCollection(fields);
    }

    public void add(SimpleField job) {
        fields.add(job);
    }
}
