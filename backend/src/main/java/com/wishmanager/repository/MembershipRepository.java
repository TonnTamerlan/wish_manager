package com.wishmanager.repository;

import com.wishmanager.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {
    
    List<Membership> findByUserId(UUID userId);
    
    List<Membership> findByWishlistId(UUID wishlistId);
    
    Optional<Membership> findByUserIdAndWishlistId(UUID userId, UUID wishlistId);
    
    boolean existsByUserIdAndWishlistId(UUID userId, UUID wishlistId);
    
    @Query("SELECT m FROM Membership m WHERE m.wishlistId = :wishlistId AND m.role = 'OWNER'")
    Optional<Membership> findOwnerByWishlistId(@Param("wishlistId") UUID wishlistId);
    
    @Query("SELECT m FROM Membership m WHERE m.userId = :userId AND m.wishlistId = :wishlistId")
    Optional<Membership> findUserMembershipInWishlist(@Param("userId") UUID userId, @Param("wishlistId") UUID wishlistId);
}
