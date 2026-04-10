package com.sloms.service;

import com.sloms.dto.LoginRequest;
import com.sloms.model.User;
import com.sloms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        
        Map<String, Object> response = new HashMap<>();
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(request.getPassword())) {
            response.put("success", true);
            response.put("user", userOpt.get());
            response.put("message", "Login successful");
        } else {
            response.put("success", false);
            response.put("message", "Invalid email or password");
        }
        return response;
    }

    public Optional<User> getAdminProfile() {
        return userRepository.findByRole("admin").stream().findFirst();
    }

    public User updateAdminProfile(User updatedAdmin) {
        if (updatedAdmin.getId() == null) {
            throw new RuntimeException("Admin ID is required");
        }
        User admin = userRepository.findById(updatedAdmin.getId())
                .orElse(userRepository.findByRole("admin").stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Admin not found")));
        
        admin.setName(updatedAdmin.getName());
        admin.setEmail(updatedAdmin.getEmail());
        if (updatedAdmin.getPassword() != null && !updatedAdmin.getPassword().isEmpty()) {
            admin.setPassword(updatedAdmin.getPassword());
        }
        return userRepository.save(admin);
    }
}
