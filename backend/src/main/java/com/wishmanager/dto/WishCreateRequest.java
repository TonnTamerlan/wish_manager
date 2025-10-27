package com.wishmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishCreateRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    private String links; // JSON array of URLs
}
