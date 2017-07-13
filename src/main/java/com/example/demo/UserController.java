package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User findOne(@PathVariable("id") Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
//        User user = userRepository.getOne(id);
//        if (user == null) {
//            throw new UserNotFoundException();
//        }
//        return user;
    }

    @PostMapping("/users")
    public User save(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
    }
}
