package com.wishmanager.controller;

import com.wishmanager.dto.AuthRequest;
import com.wishmanager.dto.AuthResponse;
import com.wishmanager.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/telegram")
    public ResponseEntity<AuthResponse> authenticateTelegram(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticateTelegram(request.getInitData());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/google")
    public ResponseEntity<AuthResponse> authenticateGoogle(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticateGoogle(request.getCode(), request.getVerifier());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        // This will be implemented with JWT authentication
        return ResponseEntity.ok("User profile endpoint");
    }
}
