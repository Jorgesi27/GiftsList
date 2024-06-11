package com.example.application.controllers;

import com.example.application.domain.Usuario;
import com.example.application.services.UserManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.UUID;

@RestController
public class UserRestController{

    private final UserManagementService service;

    public UserRestController(UserManagementService service) {
        this.service = service;
    }

    @GetMapping("/api/users")
    public List<Usuario> all() {
        return service.loadActiveUsers();
    }

    /*@PostMapping("/api/users")
    void newUser(@RequestBody Usuario newUser) {
        service.registerUser(newUser);

    }*/

    @GetMapping("/api/users/{id}")
    Usuario one(@PathVariable String id) {
        return service.loadUserById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
    }

    @PutMapping("/api/users/{id}")
    Usuario replaceUser(@RequestBody Usuario newUser, @PathVariable Integer id) {
        return newUser;
    }

    @DeleteMapping("/api/users/{id}")
    void deleteUser(@PathVariable Integer id) {}
}