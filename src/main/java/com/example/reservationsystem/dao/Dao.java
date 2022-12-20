package com.example.reservationsystem.dao;

public interface Dao<I, T> {
    T getById(I id);
    void insert(T entity);
    void updateById(String id, T entity);
    void deleteById(String id);
}
