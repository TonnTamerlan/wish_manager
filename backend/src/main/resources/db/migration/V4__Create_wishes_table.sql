-- V4__Create_wishes_table.sql
CREATE TABLE wishes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wishlist_id UUID NOT NULL REFERENCES wishlists(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    links JSONB, -- Array of URL strings
    status VARCHAR(20) NOT NULL DEFAULT 'FREE' CHECK (status IN ('FREE', 'BOOKED', 'GIFTED')),
    booked_by UUID REFERENCES users(id) ON DELETE SET NULL,
    hide_booker_name BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_wishes_wishlist_id ON wishes(wishlist_id);
CREATE INDEX idx_wishes_status ON wishes(status);
CREATE INDEX idx_wishes_booked_by ON wishes(booked_by);
CREATE INDEX idx_wishes_created_at ON wishes(created_at);

-- Create GIN index for JSONB links field
CREATE INDEX idx_wishes_links_gin ON wishes USING GIN (links);

-- Create trigger to update updated_at timestamp
CREATE TRIGGER update_wishes_updated_at 
    BEFORE UPDATE ON wishes 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();
