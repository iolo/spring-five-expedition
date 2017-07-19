package com.example.demo

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserHandlerTest {

    var port: Int = 0 @LocalServerPort set

    lateinit var template: TestRestTemplate @Autowired set

    lateinit var userRepository: UserRepository @Autowired set


    @Before
    fun setUp() {
        userRepository.save(User(name = "foo", email = "foo@example.com"))
        userRepository.save(User(name = "bar", email = "bar@example.com"))
        userRepository.save(User(name = "baz", email = "baz@example.com"))
        userRepository.save(User(name = "qux", email = "qux@example.com"))
    }

    @Test
    @Throws(Exception::class)
    fun run() {
        val endpoint = "http://localhost:$port"

        val findAllResponse = template.getForEntity(endpoint + "/users", Array<User>::class.java)
        val users = findAllResponse.body
        println(Arrays.toString(users))
        assertEquals(4, users!!.size.toLong())
        assertEquals("foo", users[0].name)
        assertEquals("bar", users[1].name)
        assertEquals("baz", users[2].name)
        assertEquals("qux", users[3].name)

        var findOneResponse = template.getForEntity(endpoint + "/users/{id}", User::class.java, 1)
        var user: User = findOneResponse.body
        println(user)
        assertEquals("foo", user.name)

        findOneResponse = template.getForEntity(endpoint + "/users/{id}", User::class.java, 1234)
        assertEquals(404, findOneResponse.statusCode.value().toLong())

        findOneResponse = template.getForEntity(endpoint + "/users/{id}", User::class.java, "hello")
        assertEquals(400, findOneResponse.statusCode.value().toLong())

        user = User(name = "hello", email = "hello@example.com")
        val response = template.postForEntity(endpoint + "/users", user, User::class.java)
        user = response.body
        println(user)
        assertEquals("hello", user.name)

        template.delete(endpoint + "/users/{id}", 1)
        findOneResponse = template.getForEntity(endpoint + "/users/{id}", User::class.java, 1)
        assertEquals(404, findOneResponse.statusCode.value().toLong())
    }
}