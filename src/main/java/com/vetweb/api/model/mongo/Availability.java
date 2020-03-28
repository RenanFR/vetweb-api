package com.vetweb.api.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Availability {
	
	private boolean enabled;
	
	private String text;
	
	private WeekDay sunday;
	
	private WeekDay monday;
	
	private WeekDay tuesday;
	
	private WeekDay wednesday;
	
	private WeekDay thursday;
	
	private WeekDay friday;
	
	private WeekDay saturday; 

}
