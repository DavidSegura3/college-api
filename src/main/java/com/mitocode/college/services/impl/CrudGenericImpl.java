package com.mitocode.college.services.impl;


import com.mitocode.college.repositories.IGenericRepository;
import com.mitocode.college.services.IGenericCrud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class CrudGenericImpl<T, ID> implements IGenericCrud<T, ID> {

    protected abstract IGenericRepository<T, ID> getRepository();

    @Override
    public Mono<T> save(T t) {
        return getRepository().save(t);
    }

    @Override
    public Mono<T> update(T t, ID id) {
        return getRepository().findById(id)
                .flatMap(e -> getRepository().save(t));
    }

    @Override
    public Flux<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public Mono<T> findById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public Mono<Boolean> deleteById(ID id) {
        return getRepository().findById(id)
                .hasElement()
                .flatMap(result -> {
                    if (result) {
                        return getRepository().deleteById(id).thenReturn(true);
                    } else {
                        return Mono.just(false);
                    }
                });
    }
}
