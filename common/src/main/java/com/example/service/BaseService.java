package com.example.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID, Req, Res> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    void deleteById(ID id);
    boolean existsById(ID id);
}
