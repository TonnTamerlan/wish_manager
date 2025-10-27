-- V5__Create_initial_data.sql
-- Insert sample data for development/testing

-- Insert sample users
INSERT INTO users (telegram_id, display_name, avatar_url) VALUES
('123456789', 'John Doe', 'https://example.com/avatar1.jpg'),
('987654321', 'Jane Smith', 'https://example.com/avatar2.jpg'),
('555666777', 'Bob Johnson', 'https://example.com/avatar3.jpg');

-- Insert sample wishlists
INSERT INTO wishlists (owner_id, title, description, is_public) VALUES
((SELECT id FROM users WHERE telegram_id = '123456789'), 'My Birthday Wishlist', 'Things I would love for my birthday', true),
((SELECT id FROM users WHERE telegram_id = '987654321'), 'Christmas Wishes', 'Holiday gift ideas', false),
((SELECT id FROM users WHERE telegram_id = '555666777'), 'Tech Gadgets', 'Cool tech stuff I want', true);

-- Insert sample memberships
INSERT INTO memberships (user_id, wishlist_id, role) VALUES
((SELECT id FROM users WHERE telegram_id = '123456789'), (SELECT id FROM wishlists WHERE title = 'My Birthday Wishlist'), 'OWNER'),
((SELECT id FROM users WHERE telegram_id = '987654321'), (SELECT id FROM wishlists WHERE title = 'Christmas Wishes'), 'OWNER'),
((SELECT id FROM users WHERE telegram_id = '555666777'), (SELECT id FROM wishlists WHERE title = 'Tech Gadgets'), 'OWNER');

-- Insert sample wishes
INSERT INTO wishes (wishlist_id, name, description, links, status) VALUES
((SELECT id FROM wishlists WHERE title = 'My Birthday Wishlist'), 'iPhone 15 Pro', 'Latest iPhone with amazing camera', '["https://apple.com/iphone-15-pro"]', 'FREE'),
((SELECT id FROM wishlists WHERE title = 'My Birthday Wishlist'), 'AirPods Pro', 'Wireless earbuds with noise cancellation', '["https://apple.com/airpods-pro"]', 'BOOKED'),
((SELECT id FROM wishlists WHERE title = 'Christmas Wishes'), 'Nintendo Switch', 'Gaming console for family fun', '["https://nintendo.com/switch"]', 'FREE'),
((SELECT id FROM wishlists WHERE title = 'Tech Gadgets'), 'MacBook Pro', 'Powerful laptop for development', '["https://apple.com/macbook-pro"]', 'GIFTED');
