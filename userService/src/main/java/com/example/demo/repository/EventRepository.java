package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Category;
import com.example.demo.entity.Event;




@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

	
	List<Event> findByStatusIn(List<String> status);
	
	List<Event> findByNameContainingIgnoreCaseAndStatusIn(String eventName, List<String> status);
	
	List<Event> findByCategoryAndStatusIn(Category category, List<String> status);

}
