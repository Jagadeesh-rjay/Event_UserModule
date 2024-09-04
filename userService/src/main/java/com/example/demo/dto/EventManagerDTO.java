package com.example.demo.dto;

import org.springframework.beans.BeanUtils;

import com.example.demo.entity.Address;
import com.example.demo.entity.EventManager;

import lombok.Data;

@Data
public class EventManagerDTO {
	
	private Long id;

	private String firstName;

	private String lastName;

	private String emailId;
	
	private String eventName;

	private Long mob;

	private String role;

	private Address address;

	private EventManager eventManager;

	private String status;
	
	public static EventManagerDTO toUserDtoEntity(EventManager user) {
		EventManagerDTO userDto = new EventManagerDTO();
		BeanUtils.copyProperties(user, userDto, "manager");
		return userDto;
	}

}
