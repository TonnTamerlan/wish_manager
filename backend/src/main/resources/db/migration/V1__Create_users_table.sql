-- V1__Create_users_table.sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    telegram_id VARCHAR(255) UNIQUE,
    google_sub VARCHAR(255) UNIQUE,
    display_name VARCHAR(255) NOT NULL,
    avatar_url TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT check_auth_provider CHECK (
        (telegram_id IS NOT NULL AND google_sub IS NULL) OR
        (telegram_id IS NULL AND google_sub IS NOT NULL)
    )
);

-- Create indexes for better performance
CREATE INDEX idx_users_telegram_id ON users(telegram_id);
CREATE INDEX idx_users_google_sub ON users(google_sub);
CREATE INDEX idx_users_created_at ON users(created_at);
