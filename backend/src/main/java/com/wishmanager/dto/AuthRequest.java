package com.wishmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    
    @NotBlank(message = "Init data is required for Telegram authentication")
    private String initData;
    
    private String code; // For Google OAuth
    private String verifier; // For Google OAuth PKCE
}
