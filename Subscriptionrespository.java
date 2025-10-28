package com.scb.rwtoolbackend.repository;

import com.scb.rwtoolbackend.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // Correct field names based on your entity
    Optional<Subscription> findByUsernameAndGroupName(String username, String groupName);

    // Optional helper methods
    List<Subscription> findByUsername(String username);
    List<Subscription> findByGroupName(String groupName);
}
