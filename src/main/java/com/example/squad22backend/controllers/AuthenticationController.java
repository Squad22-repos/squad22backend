package com.example.squad22backend.controllers;

import com.example.squad22backend.dtos.AuthenticationDTO;
import com.example.squad22backend.dtos.LoginResponseDTO;
import com.example.squad22backend.dtos.UserRegisterDTO;
import com.example.squad22backend.infra.security.TokenService;
import com.example.squad22backend.models.User;
import com.example.squad22backend.repositories.UserRepository;
import com.example.squad22backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRegisterDTO user) {
        if (this.userRepository.findUserByUsername(user.username()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
        this.userService.addUser(user, encryptedPassword);

        return ResponseEntity.ok().build();
    }
}
