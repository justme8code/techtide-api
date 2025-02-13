package com.justme8code.techtide.controllers;


import com.justme8code.techtide.models.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    public String addUser(@RequestBody User user) {

        return "User added successfully";
    }
}
