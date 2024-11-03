package com.weatherapi.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherRespoDto {
	 private String pincode;
	    private LocalDate forDate;
	    private JsonNode weatherData;
}
