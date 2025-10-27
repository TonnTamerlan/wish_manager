package com.wishmanager.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wishmanager.entity.User;
import com.wishmanager.entity.Wishlist;
import com.wishmanager.entity.Wish;
import com.wishmanager.repository.UserRepository;
import com.wishmanager.repository.WishlistRepository;
import com.wishmanager.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotService {
    
    private final WishlistRepository wishlistRepository;
    // Future use: UserRepository and MembershipRepository for advanced features
    @SuppressWarnings("unused")
    private final UserRepository userRepository;
    @SuppressWarnings("unused")
    private final MembershipRepository membershipRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Value("${telegram.bot.token}")
    private String botToken;
    
    @Value("${telegram.bot.username}")
    private String botUsername;
    
    @Value("${telegram.bot.webapp-url}")
    private String webappUrl;
    
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot";
    private static final String SEND_MESSAGE_ENDPOINT = "/sendMessage";
    private static final String EDIT_MESSAGE_TEXT_ENDPOINT = "/editMessageText";
    
    /**
     * Handles incoming Telegram webhook updates
     * @param updateJson Raw JSON string from Telegram webhook
     */
    public void handleUpdate(String updateJson) {
        try {
            log.debug("Received webhook update: {}", updateJson);
            
            JsonNode update = objectMapper.readTree(updateJson);
            
            // Handle different types of updates
            if (update.has("message")) {
                handleMessage(update.get("message"));
            } else if (update.has("callback_query")) {
                handleCallbackQuery(update.get("callback_query"));
            } else if (update.has("inline_query")) {
                handleInlineQuery(update.get("inline_query"));
            } else {
                log.debug("Unhandled update type: {}", update);
            }
            
        } catch (Exception e) {
            log.error("Error processing webhook update: {}", updateJson, e);
        }
    }
    
    /**
     * Handles incoming messages from users
     */
    private void handleMessage(JsonNode message) {
        try {
            JsonNode chat = message.get("chat");
            JsonNode from = message.get("from");
            
            if (chat == null || from == null) {
                log.warn("Message missing chat or from information");
                return;
            }
            
            long chatId = chat.get("id").asLong();
            long userId = from.get("id").asLong();
            String text = message.has("text") ? message.get("text").asText() : null;
            
            log.info("Received message from user {} in chat {}: {}", userId, chatId, text);
            
            // Handle different commands
            if (text != null) {
                if (text.startsWith("/start")) {
                    handleStartCommand(chatId, userId, text);
                } else if (text.startsWith("/help")) {
                    handleHelpCommand(chatId, userId);
                } else {
                    handleUnknownCommand(chatId, userId, text);
                }
            }
            
        } catch (Exception e) {
            log.error("Error handling message", e);
        }
    }
    
    /**
     * Handles callback queries from inline keyboards
     */
    private void handleCallbackQuery(JsonNode callbackQuery) {
        try {
            JsonNode message = callbackQuery.get("message");
            JsonNode data = callbackQuery.get("data");
            String callbackQueryId = callbackQuery.get("id").asText();
            
            if (message == null || data == null) {
                log.warn("Callback query missing message or data");
                return;
            }
            
            long chatId = message.get("chat").get("id").asLong();
            String callbackData = data.asText();
            
            log.info("Received callback query: {} in chat {}", callbackData, chatId);
            
            // Handle different callback data
            if (callbackData.startsWith("wishlist_")) {
                handleWishlistCallback(chatId, callbackData, callbackQueryId);
            } else if (callbackData.equals("refresh")) {
                handleRefreshCallback(chatId, callbackQueryId);
            }
            
        } catch (Exception e) {
            log.error("Error handling callback query", e);
        }
    }
    
    /**
     * Handles inline queries for quick access to wishlists
     */
    private void handleInlineQuery(JsonNode inlineQuery) {
        try {
            String queryId = inlineQuery.get("id").asText();
            long userId = inlineQuery.get("from").get("id").asLong();
            String query = inlineQuery.has("query") ? inlineQuery.get("query").asText() : "";
            
            log.info("Received inline query from user {}: {}", userId, query);
            
            // For now, just answer with empty results
            // In the future, this could search user's wishlists
            answerInlineQuery(queryId, new JsonNode[0]);
            
        } catch (Exception e) {
            log.error("Error handling inline query", e);
        }
    }
    
    /**
     * Handles /start command
     */
    private void handleStartCommand(long chatId, long userId, String command) {
        try {
            String welcomeMessage = "🎁 Welcome to Wish Manager Bot!\n\n" +
                    "I can help you manage your wishlists and send notifications about wish activities.\n\n" +
                    "Available commands:\n" +
                    "/start - Show this welcome message\n" +
                    "/help - Show help information\n" +
                    "/mywishlists - Show your wishlists\n\n" +
                    "You can also use the web app by clicking the button below:";
            
            Map<String, Object> keyboard = createWebAppKeyboard();
            
            sendMessage(chatId, welcomeMessage, keyboard);
            
        } catch (Exception e) {
            log.error("Error handling start command", e);
        }
    }
    
    /**
     * Handles /help command
     */
    private void handleHelpCommand(long chatId, long userId) {
        try {
            String helpMessage = "📚 Wish Manager Bot Help\n\n" +
                    "Commands:\n" +
                    "• /start - Welcome message and setup\n" +
                    "• /help - Show this help message\n" +
                    "• /mywishlists - List your wishlists\n\n" +
                    "Features:\n" +
                    "• Create and manage wishlists\n" +
                    "• Invite friends to your wishlists\n" +
                    "• Get notifications when wishes are booked or gifted\n" +
                    "• Access your wishlists via the web app\n\n" +
                    "Use the web app button below to access all features!";
            
            Map<String, Object> keyboard = createWebAppKeyboard();
            
            sendMessage(chatId, helpMessage, keyboard);
            
        } catch (Exception e) {
            log.error("Error handling help command", e);
        }
    }
    
    /**
     * Handles unknown commands
     */
    private void handleUnknownCommand(long chatId, long userId, String command) {
        try {
            String response = "❓ Unknown command: " + command + "\n\n" +
                    "Use /help to see available commands or click the web app button to access all features.";
            
            Map<String, Object> keyboard = createWebAppKeyboard();
            
            sendMessage(chatId, response, keyboard);
            
        } catch (Exception e) {
            log.error("Error handling unknown command", e);
        }
    }
    
    /**
     * Handles wishlist-related callbacks
     */
    private void handleWishlistCallback(long chatId, String callbackData, String callbackQueryId) {
        try {
            // Extract wishlist ID from callback data
            String[] parts = callbackData.split("_");
            if (parts.length >= 2) {
                UUID wishlistId = UUID.fromString(parts[1]);
                Optional<Wishlist> wishlistOpt = wishlistRepository.findById(wishlistId);
                
                if (wishlistOpt.isPresent()) {
                    Wishlist wishlist = wishlistOpt.get();
                    String message = "📋 " + wishlist.getTitle() + "\n\n" +
                            "Description: " + (wishlist.getDescription() != null ? wishlist.getDescription() : "No description") + "\n" +
                            "Visibility: " + (wishlist.getIsPublic() ? "Public" : "Private") + "\n\n" +
                            "Click below to open in web app:";
                    
                    Map<String, Object> keyboard = createWebAppKeyboard();
                    
                    editMessage(chatId, callbackQueryId, message, keyboard);
                }
            }
            
        } catch (Exception e) {
            log.error("Error handling wishlist callback", e);
        }
    }
    
    /**
     * Handles refresh callback
     */
    private void handleRefreshCallback(long chatId, String callbackQueryId) {
        try {
            String message = "🔄 Refreshed! Use the web app button to access your wishlists.";
            Map<String, Object> keyboard = createWebAppKeyboard();
            
            editMessage(chatId, callbackQueryId, message, keyboard);
            
        } catch (Exception e) {
            log.error("Error handling refresh callback", e);
        }
    }
    
    /**
     * Sends notification to user about wish activity
     */
    public void sendWishNotification(User user, Wish wish, String action, String actorName) {
        if (user.getTelegramId() == null) {
            log.debug("User {} has no telegram ID, skipping notification", user.getId());
            return;
        }
        
        try {
            long chatId = Long.parseLong(user.getTelegramId());
            
            String message = String.format("🎁 Wish Update!\n\n" +
                    "Wish: %s\n" +
                    "Action: %s\n" +
                    "By: %s\n\n" +
                    "Click below to view the wishlist:", 
                    wish.getName(), action, actorName);
            
            Map<String, Object> keyboard = createWebAppKeyboard();
            
            sendMessage(chatId, message, keyboard);
            
        } catch (Exception e) {
            log.error("Error sending wish notification to user {}", user.getId(), e);
        }
    }
    
    /**
     * Sends notification to user about wishlist invitation
     */
    public void sendInvitationNotification(User user, Wishlist wishlist, User inviter) {
        if (user.getTelegramId() == null) {
            log.debug("User {} has no telegram ID, skipping invitation notification", user.getId());
            return;
        }
        
        try {
            long chatId = Long.parseLong(user.getTelegramId());
            
            String message = String.format("📋 You've been invited to a wishlist!\n\n" +
                    "Wishlist: %s\n" +
                    "Invited by: %s\n\n" +
                    "Click below to view the wishlist:", 
                    wishlist.getTitle(), inviter.getDisplayName());
            
            Map<String, Object> keyboard = createWebAppKeyboard();
            
            sendMessage(chatId, message, keyboard);
            
        } catch (Exception e) {
            log.error("Error sending invitation notification to user {}", user.getId(), e);
        }
    }
    
    /**
     * Creates a web app keyboard with the bot's web app URL
     */
    private Map<String, Object> createWebAppKeyboard() {
        Map<String, Object> keyboard = new HashMap<>();
        Map<String, Object> inlineKeyboard = new HashMap<>();
        
        // Create web app button
        Map<String, Object> button = new HashMap<>();
        button.put("text", "🎁 Open Wish Manager");
        button.put("web_app", Map.of("url", webappUrl));
        
        inlineKeyboard.put("inline_keyboard", new Object[][]{{button}});
        keyboard.put("reply_markup", inlineKeyboard);
        
        return keyboard;
    }
    
    /**
     * Sends a message to a chat
     */
    private void sendMessage(long chatId, String text, Map<String, Object> replyMarkup) {
        try {
            String url = TELEGRAM_API_URL + botToken + SEND_MESSAGE_ENDPOINT;
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("chat_id", chatId);
            payload.put("text", text);
            payload.put("parse_mode", "HTML");
            
            if (replyMarkup != null) {
                payload.put("reply_markup", replyMarkup);
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            
            log.debug("Sent message to chat {}: {}", chatId, text);
            
        } catch (Exception e) {
            log.error("Error sending message to chat {}", chatId, e);
        }
    }
    
    /**
     * Edits a message in a chat
     */
    private void editMessage(long chatId, String messageId, String text, Map<String, Object> replyMarkup) {
        try {
            String url = TELEGRAM_API_URL + botToken + EDIT_MESSAGE_TEXT_ENDPOINT;
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("chat_id", chatId);
            payload.put("message_id", messageId);
            payload.put("text", text);
            payload.put("parse_mode", "HTML");
            
            if (replyMarkup != null) {
                payload.put("reply_markup", replyMarkup);
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            
            log.debug("Edited message in chat {}: {}", chatId, text);
            
        } catch (Exception e) {
            log.error("Error editing message in chat {}", chatId, e);
        }
    }
    
    /**
     * Answers an inline query
     */
    private void answerInlineQuery(String queryId, JsonNode[] results) {
        try {
            String url = TELEGRAM_API_URL + botToken + "/answerInlineQuery";
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("inline_query_id", queryId);
            payload.put("results", results);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            
            log.debug("Answered inline query: {}", queryId);
            
        } catch (Exception e) {
            log.error("Error answering inline query {}", queryId, e);
        }
    }
}
