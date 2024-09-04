package com.example.demo.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.demo.entity.EventManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomEventMangerDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private List<GrantedAuthority> authorities;

    public CustomEventMangerDetails(EventManager manager) {
        email = manager.getEmailId();
        password = manager.getPassword();
        authorities = new ArrayList<>();
        String[] roles = manager.getRole().split(",");
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
