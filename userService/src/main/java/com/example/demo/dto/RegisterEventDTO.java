package com.example.demo.dto;

import org.springframework.beans.BeanUtils;

import com.example.demo.entity.EventManager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEventDTO {

	private String firstName;
	private String lastName;
	private String emailId;
	private String password;
	private String confirmPassword;
	private String eventName;
	private String role;
	private Long mob;
	private String street;
	private String city;
	private String state;
	private int pincode;
	
	public static EventManager toUserEntity(RegisterUserDTO registerUserDto) {
		EventManager user = new EventManager();
		BeanUtils.copyProperties(registerUserDto, user);
		return user;
	}


}
