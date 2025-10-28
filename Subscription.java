package com.scb.rwtoolbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ These fields must match what the repository and NotificationService expect
    private String username;      // Subscriber’s username (used for matching user)
    private String groupName;     // The group they subscribed to (Ops_Reports, Compliance_Data, etc.)
    private String status;        // Subscribed, Pending, Rejected, etc.
    private String description;   // Optional group description (for dashboard display)

    public Subscription() {}

    public Subscription(String username, String groupName, String status, String description) {
        this.username = username;
        this.groupName = groupName;
        this.status = status;
        this.description = description;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
