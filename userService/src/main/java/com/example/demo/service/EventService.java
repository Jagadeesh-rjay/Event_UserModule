package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Event;
import com.example.demo.repository.EventRepository;

@Service
public class EventService {
	
	@Autowired
	private EventRepository eventRepository;
	
		public Event addProduct(Event event) {
			return eventRepository.save(event);
		}
		
		public Event getEventById(long eventId) {

			Optional<Event> optionalEvent = eventRepository.findById(eventId);

			if (optionalEvent.isPresent()) {
				return optionalEvent.get();
			} else {
				return null;
			}
	    }	
		
		
		public Event updateEvent(Event event) {
	        return eventRepository.save(event); // Save the updated event
	    }
		
		public List<Event> getAllEventByStatusIn(List<String> status) {
			return this.eventRepository.findByStatusIn(status);
		}
		
		public List<Event> searchEventNameAndStatusIn(String eventName, List<String> status) {
			return this.eventRepository.findByNameContainingIgnoreCaseAndStatusIn(eventName, status);
		}
		
		public List<Event> getAllEventByCategoryAndStatusIn(Category category, List<String> status) {
			return this.eventRepository.findByCategoryAndStatusIn(category, status);
		}


}
