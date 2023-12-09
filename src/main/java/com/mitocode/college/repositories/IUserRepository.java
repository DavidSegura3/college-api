package com.mitocode.college.repositories;


import com.mitocode.college.models.documents.User;
import reactor.core.publisher.Mono;

public interface IUserRepository extends IGenericRepository<User, String>{

    //@Query("{username :  ?}")
    Mono<User> findOneByUsername(String username);

}
