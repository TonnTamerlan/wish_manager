package com.wishmanager.repository;

import com.wishmanager.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {
    
    List<Wishlist> findByOwnerId(UUID ownerId);
    
    @Query("SELECT w FROM Wishlist w WHERE w.isPublic = true AND w.ownerId = :ownerId")
    List<Wishlist> findPublicByOwnerId(@Param("ownerId") UUID ownerId);
    
    @Query("SELECT w FROM Wishlist w WHERE w.isPublic = true")
    List<Wishlist> findAllPublic();
}
