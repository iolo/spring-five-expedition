package com.example.demo

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import reactor.core.publisher.Flux

fun ServerRequest.pathVariableAsLong(name: String) =
        this.pathVariable(name).toLong()

@Component
class UserHandler(val userRepository: UserRepository) {

    fun findUsers(request: ServerRequest) =
            ok().contentType(APPLICATION_JSON).body<User, Flux<User>>(userRepository.findAllAsync(), User::class.java)

    fun findOne(request: ServerRequest) =
            try {
                userRepository.findByIdAsync(request.pathVariableAsLong("id"))
                        .flatMap { ok().contentType(APPLICATION_JSON).body(fromObject(it)) }
                        .switchIfEmpty(notFound().build())
            } catch (e: NumberFormatException) {
                badRequest().build()
            }

    fun save(request: ServerRequest) =
            userRepository.saveAsync(request.bodyToMono(User::class.java))
                    .flatMap { ok().contentType(APPLICATION_JSON).body(fromObject(it)) }
                    .switchIfEmpty(notFound().build())

    fun delete(request: ServerRequest) =
            try {
                ok().build(userRepository.deleteByIdAsync(request.pathVariableAsLong("id")))
            } catch (e: NumberFormatException) {
                badRequest().build()
            }

}
