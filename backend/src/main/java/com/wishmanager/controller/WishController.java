package com.wishmanager.controller;

import com.wishmanager.dto.WishCreateRequest;
import com.wishmanager.dto.WishResponse;
import com.wishmanager.service.WishService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wishes")
@CrossOrigin(origins = "*")
public class WishController {
    
    @Autowired
    private WishService wishService;
    
    @PostMapping
    public ResponseEntity<WishResponse> createWish(@Valid @RequestBody WishCreateRequest request) {
        WishResponse response = wishService.createWish(request);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<WishResponse> updateWish(@PathVariable UUID id, @Valid @RequestBody WishCreateRequest request) {
        WishResponse response = wishService.updateWish(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWish(@PathVariable UUID id) {
        wishService.deleteWish(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/book")
    public ResponseEntity<WishResponse> bookWish(@PathVariable UUID id, @RequestBody BookRequest request) {
        WishResponse response = wishService.bookWish(id, request.isHideBookerName());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/unbook")
    public ResponseEntity<WishResponse> unbookWish(@PathVariable UUID id) {
        WishResponse response = wishService.unbookWish(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/gift")
    public ResponseEntity<WishResponse> markAsGifted(@PathVariable UUID id) {
        WishResponse response = wishService.markAsGifted(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/ungift")
    public ResponseEntity<WishResponse> unmarkAsGifted(@PathVariable UUID id) {
        WishResponse response = wishService.unmarkAsGifted(id);
        return ResponseEntity.ok(response);
    }
    
    public static class BookRequest {
        private boolean hideBookerName = false;
        
        public boolean isHideBookerName() { return hideBookerName; }
        public void setHideBookerName(boolean hideBookerName) { this.hideBookerName = hideBookerName; }
    }
}
