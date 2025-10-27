-- V3__Create_memberships_table.sql
CREATE TABLE memberships (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    wishlist_id UUID NOT NULL REFERENCES wishlists(id) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL CHECK (role IN ('OWNER', 'EDITOR', 'VIEWER')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Ensure unique membership per user per wishlist
    UNIQUE(user_id, wishlist_id)
);

-- Create indexes for better performance
CREATE INDEX idx_memberships_user_id ON memberships(user_id);
CREATE INDEX idx_memberships_wishlist_id ON memberships(wishlist_id);
CREATE INDEX idx_memberships_role ON memberships(role);
CREATE INDEX idx_memberships_created_at ON memberships(created_at);

-- Create composite index for common queries
CREATE INDEX idx_memberships_user_wishlist ON memberships(user_id, wishlist_id);
