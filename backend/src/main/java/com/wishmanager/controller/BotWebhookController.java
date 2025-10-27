package com.wishmanager.controller;

import com.wishmanager.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bot")
@CrossOrigin(origins = "*")
public class BotWebhookController {
    
    @Autowired
    private BotService botService;
    
    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody String update) {
        botService.handleUpdate(update);
        return ResponseEntity.ok().build();
    }
}
