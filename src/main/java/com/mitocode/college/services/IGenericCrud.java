package com.mitocode.college.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IGenericCrud<T, ID> {
    Mono<T> save(T t);
    Mono<T> update(T t, ID id);
    Flux<T> findAll();
    Mono<T> findById(ID id);
    Mono<Boolean> deleteById(ID id);
    //Mono<PageSupport<T>> getPage(Pageable pageable);
}
