package com.example.demo.dto;

import org.springframework.beans.BeanUtils;

import com.example.demo.entity.Address;
import com.example.demo.entity.User;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String role;
    private String status;
    private Long mob;
    private Address address;
    private User user;
    
    
    public static UserDTO toUserDtoEntity(User user) {
    	UserDTO userDto = new UserDTO();
		BeanUtils.copyProperties(user, userDto, "user");
		return userDto;
	}

    
}
