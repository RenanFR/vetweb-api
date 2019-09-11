package com.vetweb.api.utils;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;

import com.vetweb.api.model.ClinicEntity;
import com.vetweb.api.model.EntityType;

public interface GenericController<E extends ClinicEntity> {
	
	ResponseEntity<E> searchById(Long id);
	
	ResponseEntity<List<E>> searchAll();
	
	default Link addSelfLink(GenericController<E> controller, ClinicEntity entity) {
		return linkTo(methodOn(controller.getClass()).searchById(entity.getEntityId())).withSelfRel();
	}
	
	default Link addListLink(GenericController<E> controller, EntityType type) {
		return linkTo(methodOn(controller.getClass()).searchAll()).withRel(String.format("List of all %s in the database", type.getDescription()));
	}	

}
