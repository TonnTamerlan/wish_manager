package com.wishmanager.dto;

import com.wishmanager.entity.Membership;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipResponse {
    
    private UUID id;
    private UUID userId;
    private UUID wishlistId;
    private Membership.Role role;
    private LocalDateTime createdAt;
}
