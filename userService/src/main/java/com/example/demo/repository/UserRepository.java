package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  
	User getUserByEmailIdAndStatus(String emailId, String status);
	
    User findByEmailId(String emailId);
    
    List<User> findByMob(Long tel);
    
    User findByEmailIdAndRoleAndStatus(String emailId, String role, String value);
}
