package com.wishmanager.repository;

import com.wishmanager.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WishRepository extends JpaRepository<Wish, UUID> {
    
    List<Wish> findByWishlistId(UUID wishlistId);
    
    List<Wish> findByWishlistIdOrderByCreatedAtDesc(UUID wishlistId);
    
    @Query("SELECT w FROM Wish w WHERE w.wishlistId = :wishlistId AND w.status = :status")
    List<Wish> findByWishlistIdAndStatus(@Param("wishlistId") UUID wishlistId, @Param("status") Wish.Status status);
    
    @Query("SELECT w FROM Wish w WHERE w.bookedBy = :userId")
    List<Wish> findByBookedBy(@Param("userId") UUID userId);
}
