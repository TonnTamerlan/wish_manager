package com.wishmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wishes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wish {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "wishlist_id", nullable = false)
    private UUID wishlistId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "links", columnDefinition = "JSONB")
    private String links; // JSON array of URLs
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.FREE;
    
    @Column(name = "booked_by")
    private UUID bookedBy;
    
    @Column(name = "hide_booker_name", nullable = false)
    private Boolean hideBookerName = false;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public enum Status {
        FREE, BOOKED, GIFTED
    }
}
