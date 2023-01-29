package com.vvsoft.saathi.info.schema.model;

public interface Copyable<T> {
    <Z extends T> Z copy();
}
