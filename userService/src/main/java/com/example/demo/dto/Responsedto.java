package com.example.demo.dto;



import com.example.demo.entity.EventManager;
import com.example.demo.entity.User;

import lombok.Data;

@Data
public class Responsedto {

	private User user;
	private EventManager buyer;
	private String message;
}
