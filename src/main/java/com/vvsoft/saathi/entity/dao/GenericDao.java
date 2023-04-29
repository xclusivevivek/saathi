package com.vvsoft.saathi.entity.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    T create(T entity);
    Optional<T> read(String name);
    void update(T entity);
    void delete(String name);

    List<T> getAll();
}
