package com.vvsoft.saathi.info.schema;

public abstract class AbstractInfoSchema implements InfoSchema{
    private final long id;
    private final String name;

    protected AbstractInfoSchema(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
