package com.example.demo.resource;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.demo.dto.CommonResponseAPI;
import com.example.demo.dto.EventAddRequest;
import com.example.demo.dto.EventResponseDTO;
import com.example.demo.dto.EventUpdateRequest;
import com.example.demo.entity.Category;
import com.example.demo.entity.Event;
import com.example.demo.entity.EventManager;
import com.example.demo.exception.ProductSaveFailedException;
import com.example.demo.service.CategoryService;
import com.example.demo.service.EventManagerService;
import com.example.demo.service.EventService;
import com.example.demo.utility.Constants.EventStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EventResource {

	@Autowired
	private EventService eventService;

	@Autowired
	private EventManagerService eventManagerService;

	@Autowired
	private CategoryService categoryService;

	public ResponseEntity<CommonResponseAPI> addEvent(EventAddRequest eventDto) {
		log.info("Request received for Event add");

		CommonResponseAPI response = new CommonResponseAPI();

		 // Find EventManager and Category
	    EventManager eventManager = eventManagerService.findEventManagerById(eventDto.getEventManagerId());
	    if (eventManager == null) {
	        response.setResponseMessage("EventManager not found");
	        response.setSuccess(false);
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	    Category category = categoryService.getCategoryById(eventDto.getCategoryId());
	    if (category == null) {
	        response.setResponseMessage("Category not found");
	        response.setSuccess(false);
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	    // Convert DTO to Entity
	    Event event = EventAddRequest.toEntity(eventDto, category, eventManager);
	    event.setStatus(EventStatus.ACTIVE.value());

	    
	    // Save Event
	    Event savedEvent = eventService.addProduct(event);
	    
	    if (savedEvent == null) {
	        throw new ProductSaveFailedException("Failed to save the event");
	    }

	    response.setResponseMessage("Event added successfully");
	    response.setSuccess(true);

		return new ResponseEntity<CommonResponseAPI>(response, HttpStatus.OK);
	}
	
	public ResponseEntity<CommonResponseAPI> updateEventDetail(EventUpdateRequest request) {
	    log.info("Request received for updating event with ID: {}", request.getId());

	    CommonResponseAPI response = new CommonResponseAPI();

	    if (request == null || request.getId() == 0) { // Ensure the request and event ID are not null/invalid
	        response.setResponseMessage("Missing input or invalid event ID");
	        response.setSuccess(false);
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	    Event event = this.eventService.getEventById(request.getId());

	    if (event == null) { // If the event does not exist
	        response.setResponseMessage("Event not found");
	        response.setSuccess(false);
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }

	    // Update event details from request
	    if (request.getName() != null) {
	        event.setName(request.getName());
	    }
	    if (request.getDescription() != null) {
	        event.setDescription(request.getDescription());
	    }
	    if (request.getLocation() != null) {
	        event.setLocation(request.getLocation());
	    }

	    // Update category if provided
	    Category categoryById = this.categoryService.getCategoryById(request.getCategoryId());
	    if (categoryById != null) {
	        event.setCategory(categoryById);
	        log.info("Updated event category to ID: {}", categoryById.getId());
	    } else if (request.getCategoryId() != 0) {
	        log.warn("Category with ID: {} not found, keeping original category", request.getCategoryId());
	    }

	    Event updatedEvent = this.eventService.updateEvent(event);

	    if (updatedEvent == null) { // If updating fails
	        throw new RuntimeException("Failed to update the event details");
	    }

	    response.setResponseMessage("Event updated successfully");
	    response.setSuccess(true);


		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	public ResponseEntity<EventResponseDTO> fetchEventById(Integer eventId) {

		log.info("request received for searching the Events");

		EventResponseDTO response = new EventResponseDTO();

		 if (eventId == null || eventId <= 0) {
		        response.setResponseMessage("Missing or invalid event ID");
		        response.setSuccess(false);
		        return new ResponseEntity<EventResponseDTO>(response, HttpStatus.BAD_REQUEST);
		    }

		  Event event = this.eventService.getEventById(eventId.longValue());

		    if (event == null) {
		        response.setResponseMessage("Event not found");
		        response.setSuccess(false);
		        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		    }

		    response.setEvent(Arrays.asList(event));
		    response.setResponseMessage("Event fetched successfully");
		    response.setSuccess(true);

		    return new ResponseEntity<EventResponseDTO>(response, HttpStatus.OK);
		}
	
	
	public ResponseEntity<EventResponseDTO> fetchAllEvent() {
		log.info("request received for fetching all the Event");

		EventResponseDTO response = new EventResponseDTO();

		List<Event> event = this.eventService
				.getAllEventByStatusIn(Arrays.asList(EventStatus.ACTIVE.value()));

		if (CollectionUtils.isEmpty(event)) {
			response.setResponseMessage("No Event found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDTO>(response, HttpStatus.OK);
		}

		response.setEvent(event);
		response.setResponseMessage("Event fetched success");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDTO>(response, HttpStatus.OK);
	}
	
	
	public ResponseEntity<EventResponseDTO> searchEventByName(String eventName) {
		log.info("request received for searching the Event");

		EventResponseDTO response = new EventResponseDTO();

		if (eventName == null) {
			response.setResponseMessage("missing input, Event name");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDTO>(response, HttpStatus.BAD_REQUEST);
		}

		List<Event> event = this.eventService.searchEventNameAndStatusIn(eventName,
				Arrays.asList(EventStatus.ACTIVE.value()));

		if (CollectionUtils.isEmpty(event)) {
			response.setResponseMessage("No Event found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDTO>(response, HttpStatus.OK);
		}

		response.setEvent(event);
		response.setResponseMessage("Event fetched success");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDTO>(response, HttpStatus.OK);

	}
	
	public ResponseEntity<EventResponseDTO> fetchAllEventsByCategory(int categoryId) {
		log.info("request received for fetching all the Event by category");

		EventResponseDTO response = new EventResponseDTO();

		if (categoryId == 0) {
			response.setResponseMessage("category id missing");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDTO>(response, HttpStatus.BAD_REQUEST);
		}

		Category category = this.categoryService.getCategoryById(categoryId);

		if (category == null) {
			response.setResponseMessage("category not found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDTO>(response, HttpStatus.BAD_REQUEST);
		}

		List<Event> event = this.eventService.getAllEventByCategoryAndStatusIn(category,
				Arrays.asList(EventStatus.ACTIVE.value()));

		if (CollectionUtils.isEmpty(event)) {
			response.setResponseMessage("No Event found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDTO>(response, HttpStatus.OK);
		}

		response.setEvent(event);
		response.setResponseMessage("Event fetched success");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDTO>(response, HttpStatus.OK);
	}




}
