package com.mitocode.college.services.impl;

import com.mitocode.college.models.documents.User;
import com.mitocode.college.repositories.IGenericRepository;
import com.mitocode.college.repositories.IRoleRepository;
import com.mitocode.college.repositories.IUserRepository;
import com.mitocode.college.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends CrudGenericImpl<User, String> implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final BCryptPasswordEncoder bcrypt;

    protected IGenericRepository<User, String> getRepository() {
        return userRepository;
    }

    @Override
    public Mono<User> saveHash(User user) {
        user.setPassword(bcrypt.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Mono<com.mitocode.college.models.auth.User> searchByUser(String username) {
        Mono<User> monoUser = userRepository.findOneByUsername(username);
        List<String> roles = new ArrayList<>();

        return monoUser.flatMap(u -> {
            return Flux.fromIterable(u.getRoles())
                    .flatMap(rol -> {
                        return roleRepository.findById(rol.getId())
                                .map(r -> {
                                    roles.add(r.getName());
                                    return r;
                                });
                    }).collectList().flatMap(list -> {
                        u.setRoles(list);
                        return Mono.just(u);
                    });
        }).flatMap(u -> Mono.just(new com.mitocode.college.models.auth.User(u.getUsername(), u.getPassword(), u.isStatus(), roles)));
    }
}