package com.example.demo

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class User(
        @Id
        @GeneratedValue
        var id: Long? = null,
        @Column
        var name: String? = null,
        @Column
        var email: String? = null
)
