package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.EventManager;

@Repository
public interface EventManagerDao extends JpaRepository<EventManager, Long> {

	EventManager getUserByEmailIdAndStatus(String emailId, String status);

	EventManager getEventManagerByEmailId(String emailId);

	List<EventManager> findByMob(Long tel);

	EventManager findByEmailId(String emailId);

	EventManager findByEmailIdAndRoleAndStatus(String emailId, String role, String value);

	EventManager findEventManagerById(Long eventManagerId);
}
