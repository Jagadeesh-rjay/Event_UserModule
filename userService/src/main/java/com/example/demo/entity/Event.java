package com.example.demo.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
	@Size(min = 1, max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "location cannot be null")
    private String location;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ManyToOne
	// (fetch = FetchType.EAGER)
	@JoinColumn(name = "eventManager_id")
	private EventManager eventManager;	

	@NotBlank(message = "Status is mandatory")
	private String status;

    

   
}
