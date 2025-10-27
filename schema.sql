-- Wish Manager Database Schema
-- PostgreSQL DDL statements based on the ER diagram

-- Create database (run this separately if needed)
-- CREATE DATABASE wish_manager;

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    telegram_id VARCHAR(255) UNIQUE, -- nullable for Web App only users
    google_sub VARCHAR(255) UNIQUE,  -- nullable for Mini App only users
    display_name VARCHAR(255) NOT NULL,
    avatar_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    -- Ensure at least one auth method is provided
    CONSTRAINT check_auth_method CHECK (
        telegram_id IS NOT NULL OR google_sub IS NOT NULL
    )
);

-- Wishlists table
CREATE TABLE wishlists (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    is_public BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Memberships table (many-to-many relationship between users and wishlists)
CREATE TABLE memberships (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    wishlist_id UUID NOT NULL REFERENCES wishlists(id) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL CHECK (role IN ('OWNER', 'EDITOR', 'VIEWER')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    -- Ensure unique membership per user-wishlist combination
    UNIQUE(user_id, wishlist_id)
);

-- Wishes table
CREATE TABLE wishes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    wishlist_id UUID NOT NULL REFERENCES wishlists(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    links JSONB, -- array of URL strings
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_users_telegram_id ON users(telegram_id);
CREATE INDEX idx_users_google_sub ON users(google_sub);
CREATE INDEX idx_wishlists_owner_id ON wishlists(owner_id);
CREATE INDEX idx_wishlists_is_public ON wishlists(is_public);
CREATE INDEX idx_memberships_user_id ON memberships(user_id);
CREATE INDEX idx_memberships_wishlist_id ON memberships(wishlist_id);
CREATE INDEX idx_wishes_wishlist_id ON wishes(wishlist_id);

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers to automatically update updated_at
CREATE TRIGGER update_wishlists_updated_at 
    BEFORE UPDATE ON wishlists 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_wishes_updated_at 
    BEFORE UPDATE ON wishes 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Insert initial owner membership when creating a wishlist
CREATE OR REPLACE FUNCTION create_owner_membership()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO memberships (user_id, wishlist_id, role)
    VALUES (NEW.owner_id, NEW.id, 'OWNER');
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER create_owner_membership_trigger
    AFTER INSERT ON wishlists
    FOR EACH ROW EXECUTE FUNCTION create_owner_membership();
