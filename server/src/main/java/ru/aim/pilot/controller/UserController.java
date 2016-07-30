package ru.aim.pilot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.aim.pilot.model.User;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class UserController {

    private static final String template = "Hello, %s!";
    private AtomicLong counter = new AtomicLong(0);

    @RequestMapping("/greeting")
    public User greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new User(counter.incrementAndGet(), String.format(template, name), "pass", null);
    }
}
