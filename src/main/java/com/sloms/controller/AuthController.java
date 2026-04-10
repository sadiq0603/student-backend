package com.sloms.controller;

import com.sloms.dto.LoginRequest;
import com.sloms.model.User;
import com.sloms.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Map<String, Object> response = authService.login(request);
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAdminProfile() {
        Optional<User> admin = authService.getAdminProfile();
        return admin.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/admin")
    public ResponseEntity<?> updateAdminProfile(@RequestBody User updatedAdmin) {
        try {
            return ResponseEntity.ok(authService.updateAdminProfile(updatedAdmin));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
