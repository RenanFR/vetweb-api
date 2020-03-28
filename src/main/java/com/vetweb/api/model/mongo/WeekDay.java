package com.vetweb.api.model.mongo;

import java.time.DayOfWeek;
import java.time.LocalTime;

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
public class WeekDay {
	
	private boolean enabled;
	
	private String name;
	
	private LocalTime startTime;
	
	private LocalTime endTime;
	
	private DayOfWeek dayOfWeek;
	
}
