package com.example.demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router

@SpringBootApplication
open class DemoApplication {

    @Bean
    open fun route(userHandler: UserHandler) = router {
        accept(APPLICATION_JSON).nest {
            GET("/users", userHandler::findUsers)
            GET("/users/{id}", userHandler::findOne)
            POST("/users", userHandler::save)
            DELETE("/users/{id}", userHandler::delete)
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(DemoApplication::class.java, *args)
}
