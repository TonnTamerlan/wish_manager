package com.wishmanager.service;

import com.wishmanager.dto.WishlistCreateRequest;
import com.wishmanager.dto.WishlistResponse;
import com.wishmanager.dto.WishResponse;
import com.wishmanager.dto.MembershipResponse;
import com.wishmanager.entity.Wishlist;
import com.wishmanager.entity.Wish;
import com.wishmanager.entity.Membership;
import com.wishmanager.repository.WishlistRepository;
import com.wishmanager.repository.WishRepository;
import com.wishmanager.repository.MembershipRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WishlistService {
    
    @Autowired
    private WishlistRepository wishlistRepository;
    
    @Autowired
    private WishRepository wishRepository;
    
    @Autowired
    private MembershipRepository membershipRepository;
    
    public WishlistResponse createWishlist(WishlistCreateRequest request) {
        log.info("Creating wishlist with title: '{}'", request.getTitle());
        log.debug("Wishlist details - description: {}, isPublic: {}", 
                  request.getDescription(), request.getIsPublic());
        
        Wishlist wishlist = new Wishlist();
        wishlist.setTitle(request.getTitle());
        wishlist.setDescription(request.getDescription());
        wishlist.setIsPublic(request.getIsPublic());
        
        // TODO: Set ownerId from current user context
        // wishlist.setOwnerId(currentUserId);
        
        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        log.debug("Wishlist saved with ID: {}", savedWishlist.getId());
        
        // Create owner membership
        Membership ownerMembership = new Membership();
        ownerMembership.setUserId(savedWishlist.getOwnerId());
        ownerMembership.setWishlistId(savedWishlist.getId());
        ownerMembership.setRole(Membership.Role.OWNER);
        membershipRepository.save(ownerMembership);
        log.debug("Created owner membership for wishlist ID: {}", savedWishlist.getId());
        
        log.info("Successfully created wishlist with ID: {}", savedWishlist.getId());
        return convertToResponse(savedWishlist);
    }
    
    public List<WishlistResponse> getWishlists(UUID owner, Boolean publicOnly) {
        log.debug("Getting wishlists - owner: {}, publicOnly: {}", owner, publicOnly);
        
        List<Wishlist> wishlists;
        
        if (owner != null) {
            if (publicOnly != null && publicOnly) {
                log.debug("Finding public wishlists for owner: {}", owner);
                wishlists = wishlistRepository.findPublicByOwnerId(owner);
            } else {
                log.debug("Finding all wishlists for owner: {}", owner);
                wishlists = wishlistRepository.findByOwnerId(owner);
            }
        } else if (publicOnly != null && publicOnly) {
            log.debug("Finding all public wishlists");
            wishlists = wishlistRepository.findAllPublic();
        } else {
            log.debug("Finding wishlists where user is a member");
            // TODO: Get wishlists where user is a member
            wishlists = List.of();
        }
        
        log.info("Found {} wishlists", wishlists.size());
        
        return wishlists.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public WishlistResponse getWishlistById(UUID id) {
        log.debug("Getting wishlist by ID: {}", id);
        
        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Wishlist not found with ID: {}", id);
                    return new RuntimeException("Wishlist not found with id: " + id);
                });
        
        log.debug("Successfully retrieved wishlist with ID: {}", id);
        return convertToResponse(wishlist);
    }
    
    public void joinWishlist(UUID wishlistId) {
        log.info("User attempting to join wishlist ID: {}", wishlistId);
        
        // TODO: Get current user ID from context
        UUID currentUserId = UUID.randomUUID(); // Placeholder
        
        if (membershipRepository.existsByUserIdAndWishlistId(currentUserId, wishlistId)) {
            log.warn("User {} is already a member of wishlist {}", currentUserId, wishlistId);
            throw new RuntimeException("User is already a member of this wishlist");
        }
        
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> {
                    log.error("Wishlist not found with ID: {}", wishlistId);
                    return new RuntimeException("Wishlist not found with id: " + wishlistId);
                });
        
        if (!wishlist.getIsPublic()) {
            log.warn("Attempt to join private wishlist {} without invitation", wishlistId);
            throw new RuntimeException("Cannot join private wishlist without invitation");
        }
        
        Membership membership = new Membership();
        membership.setUserId(currentUserId);
        membership.setWishlistId(wishlistId);
        membership.setRole(Membership.Role.VIEWER);
        membershipRepository.save(membership);
        
        log.info("User {} successfully joined wishlist {}", currentUserId, wishlistId);
    }
    
    public void inviteToWishlist(UUID wishlistId, UUID userId, String telegramId) {
        log.info("Inviting user {} to wishlist {}", userId, wishlistId);
        
        // TODO: Check if current user has permission to invite (OWNER or EDITOR)
        // TODO: Validate that the user exists or create invitation
        
        if (membershipRepository.existsByUserIdAndWishlistId(userId, wishlistId)) {
            log.warn("User {} is already a member of wishlist {}", userId, wishlistId);
            throw new RuntimeException("User is already a member of this wishlist");
        }
        
        wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> {
                    log.error("Wishlist not found with ID: {}", wishlistId);
                    return new RuntimeException("Wishlist not found with id: " + wishlistId);
                });
        
        Membership membership = new Membership();
        membership.setUserId(userId);
        membership.setWishlistId(wishlistId);
        membership.setRole(Membership.Role.VIEWER);
        membershipRepository.save(membership);
        log.debug("Created membership for user {} in wishlist {}", userId, wishlistId);
        
        // TODO: Send invitation notification via Telegram if telegramId is provided
        if (telegramId != null) {
            log.debug("Telegram ID provided for notification: {}", telegramId);
        }
        
        log.info("Successfully invited user {} to wishlist {}", userId, wishlistId);
    }
    
    public void leaveWishlist(UUID wishlistId) {
        log.info("User attempting to leave wishlist ID: {}", wishlistId);
        
        // TODO: Get current user ID from context
        UUID currentUserId = UUID.randomUUID(); // Placeholder
        
        Membership membership = membershipRepository.findByUserIdAndWishlistId(currentUserId, wishlistId)
                .orElseThrow(() -> {
                    log.warn("User {} is not a member of wishlist {}", currentUserId, wishlistId);
                    return new RuntimeException("User is not a member of this wishlist");
                });
        
        if (membership.getRole() == Membership.Role.OWNER) {
            log.warn("Owner {} attempted to leave wishlist {}", currentUserId, wishlistId);
            throw new RuntimeException("Owner cannot leave wishlist. Transfer ownership first.");
        }
        
        membershipRepository.delete(membership);
        log.info("User {} successfully left wishlist {}", currentUserId, wishlistId);
    }
    
    private WishlistResponse convertToResponse(Wishlist wishlist) {
        List<WishResponse> wishes = wishRepository.findByWishlistIdOrderByCreatedAtDesc(wishlist.getId())
                .stream()
                .map(this::convertWishToResponse)
                .collect(Collectors.toList());
        
        List<MembershipResponse> memberships = membershipRepository.findByWishlistId(wishlist.getId())
                .stream()
                .map(this::convertMembershipToResponse)
                .collect(Collectors.toList());
        
        return new WishlistResponse(
                wishlist.getId(),
                wishlist.getOwnerId(),
                wishlist.getTitle(),
                wishlist.getDescription(),
                wishlist.getIsPublic(),
                wishlist.getCreatedAt(),
                wishlist.getUpdatedAt(),
                wishes,
                memberships
        );
    }
    
    private WishResponse convertWishToResponse(Wish wish) {
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
    
    private MembershipResponse convertMembershipToResponse(Membership membership) {
        return new MembershipResponse(
                membership.getId(),
                membership.getUserId(),
                membership.getWishlistId(),
                membership.getRole(),
                membership.getCreatedAt()
        );
    }
}
