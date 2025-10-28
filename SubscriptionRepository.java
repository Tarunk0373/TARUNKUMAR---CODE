package com.yourpackage.repository;

import com.yourpackage.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // ✅ Fetch all subscriptions for a specific user
    List<Subscription> findByUsername(String username);

    // ✅ Fetch all subscriptions by group name
    List<Subscription> findByGroupName(String groupName);

    // ✅ Fetch all active (approved) subscriptions for a group
    List<Subscription> findByGroupNameAndStatus(String groupName, String status);

    // ✅ Check if a specific user is already subscribed to a specific group
    Subscription findByUsernameAndGroupName(String username, String groupName);
}
