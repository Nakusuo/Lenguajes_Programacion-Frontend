package com.minerva.infrastructure.rest.controller;


import com.minerva.domain.constants.Role;
import com.minerva.domain.entities.shared.Result;
import com.minerva.infrastructure.rest.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minerva.application.service.UserService;

// revisar si los verbos htttp corresponede con la accion que se realiza, por ejemplo login es un verbo de accion, pero register es un verbo de creacion, por lo que deberia ser un post
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Result<Role> result = userService.authenticate(request.username(), request.password());

        if (result.isFail()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result.getMessage());
        }

        String jwtToken = jwtService.generateToken(request.username, result.getData());

        AuthResponse response = new AuthResponse(
                jwtToken,
                "Bearer",
                request.username(),
                result.getData().name()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

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

        return ResponseEntity.ok("");
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

    public record AuthResponse(
        String accessToken,
        String tokenType,
        String username,
        String role
    ) {}
}
