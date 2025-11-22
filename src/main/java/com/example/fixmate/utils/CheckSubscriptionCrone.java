package com.example.fixmate.utils;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.fixmate.entities.User;
import com.example.fixmate.repositories.SubscriptionRepository;
import com.example.fixmate.repositories.UserRepository;

@Component
public class CheckSubscriptionCrone {
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(CheckSubscriptionCrone.class);

    public CheckSubscriptionCrone() {

        log.info("MyCronJob bean created utils"); // This will print at startup
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void runJob() {
        log.info("Cron Job executed at utils: {}", LocalDateTime.now());

        // Run the subscription expiry check here
        List<User> expiredUsers = userRepository.findEmployeesWhosSubscriptionExpired();

        expiredUsers.forEach(u -> u.setStatus("INACTIVE"));
        userRepository.saveAll(expiredUsers);

        log.info("Deactivated {} expired subscriptions", expiredUsers.size());
        log.info("Cron Job executed at utils: {}", java.time.LocalDateTime.now());
    }
}