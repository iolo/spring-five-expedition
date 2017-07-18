package com.example.demo;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ReactiveUserRepository {
    private final UserRepository userRepository;

    public ReactiveUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<User> findAll() {
        return Flux.fromIterable(userRepository.findAll());
    }

    public Mono<User> findById(Long id) {
        return Mono.justOrEmpty(userRepository.findById(id));
    }

    public Mono<User> save(Mono<User> user) {
        return user.flatMap(it -> Mono.justOrEmpty(userRepository.save(it)));
    }

    public Mono<Void> deleteById(Long id) {
        userRepository.deleteById(id);
        return Mono.empty();
    }
}
