package com.example.demo.dto;

import org.springframework.beans.BeanUtils;

import com.example.demo.entity.Category;
import com.example.demo.entity.Event;
import com.example.demo.entity.EventManager;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class EventAddRequest {

	private int id;

	private String name;

	private String description;

	private String location;

	private long categoryId;

	private long eventManagerId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public long getEventManagerId() {
		return eventManagerId;
	}

	public void setEventManagerId(long eventManagerId) {
		this.eventManagerId = eventManagerId;
	}

	public static Event toEntity(EventAddRequest dto,Category category, EventManager eventManager) {
		Event entity = new Event();
		log.info("Copying properties from DTO: {}", dto);

		BeanUtils.copyProperties(dto, entity, "categoryId", "eventManagerId");
		entity.setCategory(category);
		entity.setEventManager(eventManager);
		log.info("Entity after copying: {}", entity);
		return entity;
	}

}
