package com.vvsoft.saathi.entity.dao;

import java.util.Optional;

public interface GenericDao<T> {
    T create(T entity);
    Optional<T> read(String id);
    void update(T entity);
    void delete(String id);
}
