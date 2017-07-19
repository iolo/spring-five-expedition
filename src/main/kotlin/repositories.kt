package com.example.demo

import org.springframework.data.jpa.repository.JpaRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserRepository : JpaRepository<User, Long>

fun UserRepository.findAllAsync() =
        Flux.fromIterable(this.findAll())

fun UserRepository.findByIdAsync(id: Long) =
        Mono.justOrEmpty(this.findById(id))

fun UserRepository.saveAsync(user: Mono<User>) =
        user.flatMap { Mono.justOrEmpty(this.save(it)) }

fun UserRepository.deleteByIdAsync(id: Long): Mono<Void> =
        try {
            this.deleteById(id)
            Mono.empty()
        } catch(e: Exception) {
            Mono.error(e)
        }
