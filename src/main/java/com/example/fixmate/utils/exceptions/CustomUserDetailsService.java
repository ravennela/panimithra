package com.example.fixmate.utils.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.fixmate.entities.User;
import com.example.fixmate.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println("user details classed");
        User user = userRepository.findByEmailId(username);

        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }
        // {
        // System.out.println("user not found exception");
        // return new UsernameNotFoundException("User not found: " + username);
        // });
        System.out.println("user name in details service" + user.getName());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmailId())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
