package com.mitocode.college.services;


import com.mitocode.college.models.documents.User;
import reactor.core.publisher.Mono;

public interface IUserService extends IGenericCrud<User, String>{
    Mono<User> saveHash(User user);
    Mono<com.mitocode.college.models.auth.User> searchByUser(String username);
}
