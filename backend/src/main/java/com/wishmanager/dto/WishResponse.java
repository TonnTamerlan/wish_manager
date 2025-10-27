package com.wishmanager.dto;

import com.wishmanager.entity.Wish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishResponse {
    
    private UUID id;
    private UUID wishlistId;
    private String name;
    private String description;
    private String links;
    private Wish.Status status;
    private UUID bookedBy;
    private Boolean hideBookerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
