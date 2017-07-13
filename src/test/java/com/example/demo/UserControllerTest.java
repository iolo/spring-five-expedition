package com.example.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate template;

    @Autowired
    UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository.save(User.builder().name("foo").email("foo@example.com").build());
        userRepository.save(User.builder().name("bar").email("bar@example.com").build());
        userRepository.save(User.builder().name("baz").email("baz@example.com").build());
        userRepository.save(User.builder().name("qux").email("qux@example.com").build());
    }

    @Test
    public void run() throws Exception {
        String endpoint = "http://localhost:" + port;

        ResponseEntity<User[]> findAllResponse = template.getForEntity(endpoint + "/users", User[].class);
        User[] users = findAllResponse.getBody();
        System.out.println(Arrays.toString(users));
        assertEquals(4, users.length);
        assertEquals("foo", users[0].getName());
        assertEquals("bar", users[1].getName());
        assertEquals("baz", users[2].getName());
        assertEquals("qux", users[3].getName());

        ResponseEntity<User> findOneResponse = template.getForEntity(endpoint + "/users/{id}", User.class, 1);
        User user = findOneResponse.getBody();
        System.out.println(user);
        assertEquals("foo", user.getName());

        findOneResponse = template.getForEntity(endpoint + "/users/{id}", User.class, 1234);
        assertEquals(404, findOneResponse.getStatusCode().value());

        findOneResponse = template.getForEntity(endpoint + "/users/{id}", User.class, "hello");
        assertEquals(400, findOneResponse.getStatusCode().value());

        user = User.builder().name("hello").email("hello@example.com").build();
        ResponseEntity<User> response = template.postForEntity(endpoint + "/users", user, User.class);
        user = response.getBody();
        System.out.println(user);
        assertEquals("hello", user.getName());

        template.delete(endpoint + "/users/{id}", 1);
        findOneResponse = template.getForEntity(endpoint + "/users/{id}", User.class, 1);
        assertEquals(404, findOneResponse.getStatusCode().value());
    }
}