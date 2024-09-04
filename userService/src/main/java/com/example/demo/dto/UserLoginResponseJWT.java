package com.example.demo.dto;

import lombok.Data;

@Data
public class UserLoginResponseJWT extends CommonResponseAPI {
	
	private UserDTO user;
	
	private  EventManagerDTO manager;

	private String jwtToken;

	
}
