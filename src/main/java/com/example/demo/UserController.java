package com.example.demo;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserController {
    private final ReactiveUserRepository userRepository;

    public UserController(ReactiveUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public Flux<User> findUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public Mono<User> findOne(@PathVariable("id") Long id) {
        return userRepository.findById(id).switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

    @PostMapping("/users")
    public Mono<User> save(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/users/{id}")
    public Mono<Void> delete(@PathVariable("id") Long id) {
        return userRepository.deleteById(id);
    }
}
