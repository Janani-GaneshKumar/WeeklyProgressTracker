package com.janani.contentrecommendation.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "user_interactions")
public class UserInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who performed the action
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Content on which the action happened
    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    // Owner (curator) of the content
    @ManyToOne
    @JoinColumn(name = "curator_id", nullable = false)
    private User curator;

    // Type of interaction (LIKE, VIEW, BOOKMARK, SHARE)
    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_type", nullable = false)
    private InteractionType interactionType;

    // Timestamp of interaction
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    // Share platform (only applicable for SHARE)
    @Column(name = "share_platform")
    private String sharePlatform;

    // Getters and setters omitted for brevity
}
