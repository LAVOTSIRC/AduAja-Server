package com.plr.aduaja.controller;

import com.plr.aduaja.dto.LoginDTO;
import com.plr.aduaja.model.User;
import com.plr.aduaja.repository.UserRepository;
import com.plr.aduaja.security.JWTUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> loginApi(@RequestBody LoginDTO loginRequest) {

        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Eror: Email tidak ditemukan!");
        }

        User user = userOptional.get();

        boolean isPasswordMatch = passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash());

        if (!isPasswordMatch) {
            return ResponseEntity.status(401).body("Eror: Password Salah!");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole().name());

        return ResponseEntity.ok(response);
    }
}
