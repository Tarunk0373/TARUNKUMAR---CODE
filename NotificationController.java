package com.yourpackage.controller;

import com.yourpackage.model.Notification;
import com.yourpackage.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Get all notifications for a user
    @GetMapping("/{username}")
    public List<Notification> getNotifications(@PathVariable String username) {
        return notificationService.getNotificationsForUser(username);
    }

    // Manually trigger a notification (used by Ops or testing)
    @PostMapping("/push")
    public String pushNotification(@RequestParam String groupName, @RequestParam String message) {
        notificationService.notifySubscribers(groupName, message);
        return "Notification sent to subscribers of group: " + groupName;
    }

    // Mark notification as read
    @PutMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "Notification marked as read.";
    }
}
