package com.janani.contentrecommendation.controller;

import com.janani.contentrecommendation.entity.InteractionType;
import com.janani.contentrecommendation.entity.UserInteraction;
import com.janani.contentrecommendation.service.UserInteractionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/interactions")
public class UserInteractionController {

    private final UserInteractionService service;

    public UserInteractionController(UserInteractionService service) {
        this.service = service;
    }

    @PostMapping("/{contentId}/like")
    public ResponseEntity<String> likeContent(@PathVariable Long contentId,
                                              @RequestParam Long userId) {
        service.recordInteraction(userId, contentId, InteractionType.LIKE);
        return ResponseEntity.ok("Like recorded successfully");
    }

    @PostMapping("/{contentId}/view")
    public ResponseEntity<String> viewContent(@PathVariable Long contentId,
                                              @RequestParam Long userId) {
        service.recordInteraction(userId, contentId, InteractionType.VIEW);
        return ResponseEntity.ok("View recorded successfully");
    }

    @PostMapping("/{contentId}/bookmark")
    public ResponseEntity<String> bookmarkContent(@PathVariable Long contentId,
                                                  @RequestParam Long userId) {
        service.recordInteraction(userId, contentId, InteractionType.BOOKMARK);
        return ResponseEntity.ok("Bookmark recorded successfully");
    }

    @PostMapping("/{contentId}/share")
    public ResponseEntity<String> shareContent(@PathVariable Long contentId,
                                               @RequestParam Long userId,
                                               @RequestParam Long sharedUserId) {
        service.recordShare(userId, contentId, sharedUserId);
        return ResponseEntity.ok("Share recorded successfully");
    }


    @GetMapping("/activity")
    public ResponseEntity<?> getUserActivity(@RequestParam Long userId,
                                             @RequestParam(required = false) String type) {
        if (type != null) {
            InteractionType interactionType = InteractionType.valueOf(type.toUpperCase());
            List<UserInteraction> filtered = service.getUserActivityByType(userId, interactionType);
            return ResponseEntity.ok(filtered);
        } else {
            Map<String, List<UserInteraction>> grouped = service.getGroupedUserActivity(userId);
            return ResponseEntity.ok(grouped);
        }
    }
}
