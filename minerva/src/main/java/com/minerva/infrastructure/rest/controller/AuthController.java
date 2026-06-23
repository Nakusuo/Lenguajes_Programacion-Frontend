package com.minerva.infrastructure.rest.controller;


import com.minerva.domain.constants.Role;
import com.minerva.domain.entities.shared.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minerva.application.service.UserService;

// revisar si los verbos htttp corresponede con la accion que se realiza, por ejemplo login es un verbo de accion, pero register es un verbo de creacion, por lo que deberia ser un post
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        Result<Void> result = userService.authenticate(request.username(), request.password());

        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        return ResponseEntity.ok("Login exitoso");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        Result<Void> result = userService.register(
                request.dni(),
                request.names(),
                request.lastNames(),
                request.username(),
                request.password(),
                request.role()
        );

        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        return ResponseEntity.ok("Registro exitoso");
    }

    public record RegisterRequest(
            String dni,
            String names,
            String lastNames,
            String username,
            String password,
            Role role
    ) {}

    public record LoginRequest(
            String username,
            String password
    ) {}
}
