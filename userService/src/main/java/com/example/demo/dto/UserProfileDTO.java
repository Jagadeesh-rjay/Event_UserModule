package com.example.demo.dto;

import lombok.Data;

@Data
public class UserProfileDTO extends CommonResponseAPI{
    private Long id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String role;
    private String status;
    private Long mob;
    private AddressDTO address;
    
}