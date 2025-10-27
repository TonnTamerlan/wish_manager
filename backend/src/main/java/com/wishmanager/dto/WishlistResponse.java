package com.wishmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponse {
    
    private UUID id;
    private UUID ownerId;
    private String title;
    private String description;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<WishResponse> wishes;
    private List<MembershipResponse> memberships;
}
