package com.wishmanager.controller;

import com.wishmanager.dto.WishlistCreateRequest;
import com.wishmanager.dto.WishlistResponse;
import com.wishmanager.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wishlists")
@CrossOrigin(origins = "*")
public class WishlistController {
    
    @Autowired
    private WishlistService wishlistService;
    
    @PostMapping
    public ResponseEntity<WishlistResponse> createWishlist(@Valid @RequestBody WishlistCreateRequest request) {
        WishlistResponse response = wishlistService.createWishlist(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getWishlists(
            @RequestParam(required = false) UUID owner,
            @RequestParam(required = false) Boolean publicOnly) {
        List<WishlistResponse> wishlists = wishlistService.getWishlists(owner, publicOnly);
        return ResponseEntity.ok(wishlists);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WishlistResponse> getWishlist(@PathVariable UUID id) {
        WishlistResponse wishlist = wishlistService.getWishlistById(id);
        return ResponseEntity.ok(wishlist);
    }
    
    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinWishlist(@PathVariable UUID id) {
        wishlistService.joinWishlist(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/invite")
    public ResponseEntity<?> inviteToWishlist(@PathVariable UUID id, @RequestBody InviteRequest request) {
        wishlistService.inviteToWishlist(id, request.getUserId(), request.getTelegramId());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/leave")
    public ResponseEntity<?> leaveWishlist(@PathVariable UUID id) {
        wishlistService.leaveWishlist(id);
        return ResponseEntity.ok().build();
    }
    
    public static class InviteRequest {
        private UUID userId;
        private String telegramId;
        
        // Getters and setters
        public UUID getUserId() { return userId; }
        public void setUserId(UUID userId) { this.userId = userId; }
        public String getTelegramId() { return telegramId; }
        public void setTelegramId(String telegramId) { this.telegramId = telegramId; }
    }
}
