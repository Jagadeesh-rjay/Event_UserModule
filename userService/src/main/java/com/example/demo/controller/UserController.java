package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CommonResponseAPI;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterEventDTO;
import com.example.demo.dto.RegisterUserDTO;
import com.example.demo.dto.UserLoginResponseJWT;
import com.example.demo.dto.UserProfileDTO;
import com.example.demo.entity.EventManager;
import com.example.demo.entity.User;
import com.example.demo.resource.UserResource;
import com.example.demo.service.EventManagerService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserResource userResource;
	
	@Autowired
	private EventManagerService eventManagerService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/test")
	public String test() {
		return "your in User Module";
	}

	@PostMapping("/register")
	public ResponseEntity<CommonResponseAPI> registerUser(@RequestBody RegisterUserDTO userDTO) {
		return userResource.registerUser(userDTO);
	}	

	@PostMapping("/login/user")
	public ResponseEntity<UserLoginResponseJWT> login(@RequestBody LoginDTO loginDTO) {
		return userResource.login(loginDTO);
	}
		

	@GetMapping("/fetchAllUsers")
	public List<User> fetchAllUsers() {
		return userService.fetchAll();
	}
	
	@GetMapping("/fetchAllEventManagers")
	public List<EventManager> fetchAllEvent() {
		return eventManagerService.getAllEvent();
	}	
	
	@GetMapping("/getUserProfile/{id}")
	public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long id) {
		return userResource.getUserProfile(id);
	}
	
	@GetMapping("/getManagerProfile/{id}")
	public ResponseEntity<UserProfileDTO> getManagerProfile(@PathVariable Long id) {
		return userResource.getManagerProfile(id);
	}

	@GetMapping("/getProfile/{emailId}")
	public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable String emailId) {
		return userResource.getUserProfile(emailId);
	}

	@PutMapping("/updateProfile/{emailId}")
	public ResponseEntity<CommonResponseAPI> updateProfile(@PathVariable String emailId, @RequestBody UserProfileDTO userProfileDTO) {
		return userResource.updateUserProfile(emailId, userProfileDTO);
	}
	
	@DeleteMapping("/deleteByEmailId/{emailId}")
	public ResponseEntity<CommonResponseAPI> deleteUserById(@PathVariable String emailId) {
		return userResource.deleteUserByEmail(emailId);
	}

}
