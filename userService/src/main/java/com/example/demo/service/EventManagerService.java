package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.EventManager;
import com.example.demo.entity.User;
import com.example.demo.repository.EventManagerDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventManagerService {

	@Autowired
	private EventManagerDao eventManagerDao;

	public EventManager getUserByEmailAndStatus(String emailId, String value) {

		return eventManagerDao.getUserByEmailIdAndStatus(emailId, value);
	}

	public EventManager addUser(EventManager user) {
		return eventManagerDao.save(user);
	}

	public EventManager findByEmailId(String emailId) {

		return eventManagerDao.getEventManagerByEmailId(emailId);
	}

	public List<EventManager> findByTel(Long tel) {

		return eventManagerDao.findByMob(tel);
	}

	public EventManager addEvent(EventManager event) {
		return eventManagerDao.save(event);
	}

	public List<EventManager> getAllEvent() {
		List<EventManager> all = eventManagerDao.findAll();
		return all;

	}

	public Boolean deleteById(Long id) {
		eventManagerDao.deleteById(id);
		if (eventManagerDao.findById(id) == null) {
			log.info("user not deleted");
			return false;
		} else {
			log.info("user deleted");
			return true;
		}
	}

	public void save(EventManager event) {
		eventManagerDao.save(event);

	}

	public EventManager getbyemail(String email) {
		return eventManagerDao.findByEmailId(email);
	}

	public EventManager getUserByEmailIdAndRoleAndStatus(String emailId, String role, String value) {
		return eventManagerDao.findByEmailIdAndRoleAndStatus(emailId, role, value);

	}	

	public EventManager findEventManagerById(long eventManagerId) {

		return this.eventManagerDao.findEventManagerById(eventManagerId);
	}
	
	public EventManager getUserById(long userId) {

		Optional<EventManager> optionalUser = this.eventManagerDao.findById(userId);
         //System.out.println(optionalUser.get());
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		} else {
			return null;
		}

	}
	
	public EventManager findById(Long id) {
	    return eventManagerDao.findById(id).orElse(null);
	}

}
