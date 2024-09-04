package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.EventManager;
import com.example.demo.entity.User;
import com.example.demo.repository.EventManagerDao;
import com.example.demo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User getUserByEmailAndStatus(String emailId, String value) {

		return userRepository.getUserByEmailIdAndStatus(emailId, value);
	}

	public User findByEmailId(String emailId) {

		return userRepository.findByEmailId(emailId);
	}
	
	public List<User> findByTel(Long tel) {

		return userRepository.findByMob(tel);
	}

	public User addUser(User user) {
		return userRepository.save(user);
	}

	public List<User> fetchAll() {
		List<User> all = userRepository.findAll();
		return all;

	}

	public Boolean deleteById(Long id) {
		userRepository.deleteById(id);
		if (userRepository.findById(id) == null) {
			log.info("user not deleted");
			return false;
		} else {
			log.info("user deleted");
			return true;
		}
	}

	public void save(User user) {
		userRepository.save(user);

	}
	
	public User getbyemail(String email) {
		return userRepository.findByEmailId(email);
	}
	
	public User getUserByEmailIdAndRoleAndStatus(String emailId, String role, String value) {
		return userRepository.findByEmailIdAndRoleAndStatus(emailId, role, value);
	}
	
	public User findById(Long id) {
	    return userRepository.findById(id).orElse(null);
	}

}