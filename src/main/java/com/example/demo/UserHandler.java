package com.example.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class UserHandler {
    private final ReactiveUserRepository userRepository;

    public UserHandler(ReactiveUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Mono<ServerResponse> findUsers(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON).body(userRepository.findAll(), User.class);
    }

    public Mono<ServerResponse> findOne(ServerRequest request) {
        final Long id;
        try {
            id = Long.parseLong(request.pathVariable("id"));
        } catch (NumberFormatException e) {
            return badRequest().build();
        }
        return userRepository.findById(id)
                .flatMap(it-> ok().contentType(APPLICATION_JSON).body(fromObject(it)))
                .switchIfEmpty(notFound().build());
//        return ok().contentType(APPLICATION_JSON).body(userRepository.findById(id), User.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        final Mono<User> user = request.bodyToMono(User.class);
        return userRepository.save(user)
                .flatMap(it-> ok().contentType(APPLICATION_JSON).body(fromObject(it)))
                .switchIfEmpty(notFound().build());
//        return ok().contentType(APPLICATION_JSON).body(userRepository.save(user), User.class);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        final Long id;
        try {
            id = Long.parseLong(request.pathVariable("id"));
        } catch (NumberFormatException e) {
            return badRequest().build();
        }
        return ok().build(userRepository.deleteById(id));
    }
}
