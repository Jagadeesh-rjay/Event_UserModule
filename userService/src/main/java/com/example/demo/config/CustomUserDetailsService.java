package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.entity.EventManager;
import com.example.demo.entity.User;
import com.example.demo.service.EventManagerService;
import com.example.demo.service.UserService;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private EventManagerService eventManagerService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	User user = userService.getbyemail(email);
        if (user != null) {
            return new CustomUserDetails(user);
        }

        EventManager manager = eventManagerService.getbyemail(email);
        if (manager != null) {
            return new CustomEventMangerDetails(manager);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}