package com.janani.contentrecommendation.controller;
import com.janani.contentrecommendation.entity.Content;
import com.janani.contentrecommendation.entity.InteractionType;
import com.janani.contentrecommendation.entity.User;
import com.janani.contentrecommendation.entity.UserInteraction;
import com.janani.contentrecommendation.service.UserInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interactions")
public class UserInteractionController {

    private final UserInteractionService service;

    @Autowired
    public UserInteractionController(UserInteractionService service) {
        this.service = service;
    }

    // --- USER Endpoints ---
    @PostMapping("/{contentId}/like")
    public ResponseEntity<UserInteraction> likeContent(@PathVariable Long contentId,
                                                       @RequestParam Long userId) {
        User user = new User(userId);       // assuming constructor with id
        Content content = new Content(contentId); // assuming constructor with id
        return ResponseEntity.ok(
                service.logInteraction(user, content, InteractionType.LIKE, null)
        );
    }

    @PostMapping("/{contentId}/view")
    public ResponseEntity<UserInteraction> viewContent(@PathVariable Long contentId,
                                                       @RequestParam Long userId) {
        User user = new User(userId);
        Content content = new Content(contentId);
        return ResponseEntity.ok(
                service.logInteraction(user, content, InteractionType.VIEW, null)
        );
    }

    @PostMapping("/{contentId}/bookmark")
    public ResponseEntity<UserInteraction> bookmarkContent(@PathVariable Long contentId,
                                                           @RequestParam Long userId) {
        User user = new User(userId);
        Content content = new Content(contentId);
        return ResponseEntity.ok(
                service.logInteraction(user, content, InteractionType.BOOKMARK, null)
        );
    }

    @PostMapping("/{contentId}/share")
    public ResponseEntity<UserInteraction> shareContent(@PathVariable Long contentId,
                                                        @RequestParam Long userId,
                                                        @RequestParam String platform) {
        User user = new User(userId);
        Content content = new Content(contentId);
        return ResponseEntity.ok(
                service.logInteraction(user, content, InteractionType.SHARE, platform)
        );
    }

    // --- USER Activity History ---
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserInteraction>> getUserInteractions(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getInteractionsByUser(userId));
    }

    // --- CURATOR Analytics ---
    @GetMapping("/curator/{curatorId}")
    public ResponseEntity<List<UserInteraction>> getCuratorInteractions(@PathVariable Long curatorId) {
        return ResponseEntity.ok(service.getInteractionsByCurator(curatorId));
    }

    // --- ADMIN Monitoring ---
    @GetMapping("/admin/all")
    public ResponseEntity<List<UserInteraction>> getAllInteractions() {
        return ResponseEntity.ok(service.getAllInteractions());
    }

    @GetMapping("/admin/filter")
    public ResponseEntity<List<UserInteraction>> filterInteractions(@RequestParam(required = false) Long userId,
                                                                    @RequestParam(required = false) Long curatorId,
                                                                    @RequestParam(required = false) Long contentId,
                                                                    @RequestParam(required = false) InteractionType type) {
        // For simplicity, you can implement filtering logic in service layer
        // Example: service.filterInteractions(userId, curatorId, contentId, type)
        return ResponseEntity.ok(service.getAllInteractions()); // placeholder
    }
}
