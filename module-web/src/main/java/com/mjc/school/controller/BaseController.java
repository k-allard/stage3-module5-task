package com.mjc.school.controller;

import java.util.List;

public interface BaseController<T, R, K> {
    List<R> readAll(Integer pageNumber, Integer pageSize, String sortBy);

    R readById(K id);

    R create(T createRequest);

    R update(K id, T updateRequest);

    void deleteById(K id);
}
