package com.wishmanager.repository;

import com.wishmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByTelegramId(String telegramId);
    
    Optional<User> findByGoogleSub(String googleSub);
    
    boolean existsByTelegramId(String telegramId);
    
    boolean existsByGoogleSub(String googleSub);
}
