package com.wishmanager.service;

import com.wishmanager.dto.WishCreateRequest;
import com.wishmanager.dto.WishResponse;
import com.wishmanager.entity.Wish;
import com.wishmanager.repository.WishRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class WishService {
    
    @Autowired
    private WishRepository wishRepository;
    
    public WishResponse createWish(WishCreateRequest request) {
        log.info("Creating wish with name: '{}'", request.getName());
        log.debug("Wish details - description: {}, links: {}", 
                  request.getDescription(), request.getLinks());
        
        Wish wish = new Wish();
        wish.setName(request.getName());
        wish.setDescription(request.getDescription());
        wish.setLinks(request.getLinks());
        wish.setStatus(Wish.Status.FREE);
        wish.setHideBookerName(false);
        
        // TODO: Set wishlistId from request or context
        // For now, we'll need to add wishlistId to WishCreateRequest or get it from context
        // wish.setWishlistId(request.getWishlistId());
        
        Wish savedWish = wishRepository.save(wish);
        log.info("Successfully created wish with ID: {}", savedWish.getId());
        return convertToResponse(savedWish);
    }
    
    public WishResponse updateWish(UUID id, WishCreateRequest request) {
        log.info("Updating wish with ID: {}", id);
        
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Wish not found with ID: {}", id);
                    return new RuntimeException("Wish not found with id: " + id);
                });
        
        log.debug("Updating wish {} - name: '{}', description: {}", 
                  id, request.getName(), request.getDescription());
        
        wish.setName(request.getName());
        wish.setDescription(request.getDescription());
        wish.setLinks(request.getLinks());
        
        Wish savedWish = wishRepository.save(wish);
        log.info("Successfully updated wish with ID: {}", savedWish.getId());
        return convertToResponse(savedWish);
    }
    
    public void deleteWish(UUID id) {
        log.info("Deleting wish with ID: {}", id);
        
        if (!wishRepository.existsById(id)) {
            log.error("Wish not found with ID: {}", id);
            throw new RuntimeException("Wish not found with id: " + id);
        }
        
        wishRepository.deleteById(id);
        log.info("Successfully deleted wish with ID: {}", id);
    }
    
    public WishResponse bookWish(UUID id, boolean hideBookerName) {
        log.info("Booking wish with ID: {}, hideBookerName: {}", id, hideBookerName);
        
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Wish not found with ID: {}", id);
                    return new RuntimeException("Wish not found with id: " + id);
                });
        
        if (wish.getStatus() != Wish.Status.FREE) {
            log.warn("Attempt to book wish {} with status: {}", id, wish.getStatus());
            throw new RuntimeException("Wish is already booked or gifted");
        }
        
        wish.setStatus(Wish.Status.BOOKED);
        wish.setHideBookerName(hideBookerName);
        // TODO: Set bookedBy from current user context
        // wish.setBookedBy(currentUserId);
        
        log.debug("Wish {} status changed to BOOKED", id);
        Wish savedWish = wishRepository.save(wish);
        log.info("Successfully booked wish with ID: {}", savedWish.getId());
        return convertToResponse(savedWish);
    }
    
    public WishResponse unbookWish(UUID id) {
        log.info("Unbooking wish with ID: {}", id);
        
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Wish not found with ID: {}", id);
                    return new RuntimeException("Wish not found with id: " + id);
                });
        
        if (wish.getStatus() != Wish.Status.BOOKED) {
            log.warn("Attempt to unbook wish {} with status: {}", id, wish.getStatus());
            throw new RuntimeException("Wish is not booked");
        }
        
        wish.setStatus(Wish.Status.FREE);
        wish.setBookedBy(null);
        wish.setHideBookerName(false);
        
        log.debug("Wish {} status changed to FREE", id);
        Wish savedWish = wishRepository.save(wish);
        log.info("Successfully unbooked wish with ID: {}", savedWish.getId());
        return convertToResponse(savedWish);
    }
    
    public WishResponse markAsGifted(UUID id) {
        log.info("Marking wish {} as gifted", id);
        
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Wish not found with ID: {}", id);
                    return new RuntimeException("Wish not found with id: " + id);
                });
        
        if (wish.getStatus() != Wish.Status.BOOKED) {
            log.warn("Attempt to mark wish {} as gifted with status: {}", id, wish.getStatus());
            throw new RuntimeException("Wish must be booked before marking as gifted");
        }
        
        wish.setStatus(Wish.Status.GIFTED);
        
        log.debug("Wish {} status changed to GIFTED", id);
        Wish savedWish = wishRepository.save(wish);
        log.info("Successfully marked wish with ID: {} as gifted", savedWish.getId());
        return convertToResponse(savedWish);
    }
    
    public WishResponse unmarkAsGifted(UUID id) {
        log.info("Unmarking wish {} as gifted", id);
        
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Wish not found with ID: {}", id);
                    return new RuntimeException("Wish not found with id: " + id);
                });
        
        if (wish.getStatus() != Wish.Status.GIFTED) {
            log.warn("Attempt to unmark wish {} as gifted with status: {}", id, wish.getStatus());
            throw new RuntimeException("Wish is not marked as gifted");
        }
        
        wish.setStatus(Wish.Status.BOOKED);
        
        log.debug("Wish {} status changed from GIFTED to BOOKED", id);
        Wish savedWish = wishRepository.save(wish);
        log.info("Successfully unmarked wish with ID: {} as gifted", savedWish.getId());
        return convertToResponse(savedWish);
    }
    
    private WishResponse convertToResponse(Wish wish) {
        return new WishResponse(
                wish.getId(),
                wish.getWishlistId(),
                wish.getName(),
                wish.getDescription(),
                wish.getLinks(),
                wish.getStatus(),
                wish.getBookedBy(),
                wish.getHideBookerName(),
                wish.getCreatedAt(),
                wish.getUpdatedAt()
        );
    }
}
