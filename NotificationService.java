package com.yourpackage.service;

import com.yourpackage.model.Notification;
import com.yourpackage.repository.NotificationRepository;
import com.yourpackage.repository.SubscriptionRepository;
import com.yourpackage.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    // Send notifications to all subscribed users of a specific group
    public void notifySubscribers(String groupName, String message) {
        List<Subscription> subscribers = subscriptionRepository.findByGroupNameAndStatus(groupName, "Subscribed");
        for (Subscription sub : subscribers) {
            Notification notification = new Notification(sub.getUsername(), groupName, message);
            notificationRepository.save(notification);
        }
    }

    // Get all notifications for a user
    public List<Notification> getNotificationsForUser(String username) {
        return notificationRepository.findByUsernameOrderByTimestampDesc(username);
    }

    // Mark one notification as read
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification != null) {
            notification.setReadStatus(true);
            notificationRepository.save(notification);
        }
    }
}
