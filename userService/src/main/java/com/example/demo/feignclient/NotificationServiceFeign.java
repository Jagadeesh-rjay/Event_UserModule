package com.example.demo.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.Responsedto;



@FeignClient("NotificationService")
public interface NotificationServiceFeign {
	@PostMapping("/Notification/mobilesendOtp")
    public ResponseEntity<String> sendOtp( @RequestParam Long mobileNo);
                                           
	  @PostMapping("/Notification/validateOtp")
	    public ResponseEntity<Responsedto> validateOtp(@RequestParam Long mobile, @RequestParam String otp);
	  
	  @PostMapping("/Notification/sendEmail")
	    public ResponseEntity<String> sendMail(@RequestParam String email, @RequestParam String subject, @RequestParam String body,
	                                           @RequestParam(required = false) String cc, @RequestParam(required = false) String bcc);
}