package com.example.demo.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Entity
@Data
public class EventManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Email(message = "Provide Valid E-mail - ID")
    @NotBlank(message = "Email is mandatory")
    private String emailId;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "EventName is mandatory")
    private String eventName;

    @NotBlank(message = "Role is mandatory")
    private String role;

    @NotBlank(message = "Status is mandatory")
    private String status;

    @NotNull(message = "Mobile number cannot be null")
    private Long mob;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "eventManager_id")
    @JsonIgnore // Prevents serialization cycle
    private EventManager manager;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    @JsonIgnore // Prevents serialization cycle
    private Set<EventManager> subordinates;
}
