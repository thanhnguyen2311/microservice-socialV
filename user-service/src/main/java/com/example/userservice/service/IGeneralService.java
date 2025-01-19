package com.example.userservice.service;

public interface IGeneralService<T> {
    Iterable<T> findAll();

    T findById(Long id);

    T save(T t);

    void remove(Long id);
}
