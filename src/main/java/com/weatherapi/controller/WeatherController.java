package com.weatherapi.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapi.dto.WeatherRespoDto;
import com.weatherapi.entities.Weather;
import com.weatherapi.service.IWeatherService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/weather")
@Slf4j
public class WeatherController {
	
    @Autowired
    private IWeatherService weatherService;
	
    @GetMapping("/info")
    public ResponseEntity<WeatherRespoDto> getWeather(
            @RequestParam("for_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate forDate,
            @RequestParam String pincode,
            @RequestParam(defaultValue = "IN") String country) {

        log.info("WeatherController.getWeather: Fetching weather for pincode: {}, country: {}, date: {}", pincode, country, forDate);

        Weather data = weatherService.getWeatherInfo(pincode, country, forDate);

        try {
        	ObjectMapper objectMapper = new ObjectMapper();
			JsonNode weatherJson = objectMapper.readTree(data.getWeatherData());

            WeatherRespoDto responseDto = new WeatherRespoDto();
            responseDto.setPincode(data.getPincode().getPincode());
            responseDto.setForDate(data.getForDate());
            responseDto.setWeatherData(weatherJson);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("WeatherController.getWeather: Failed to parse weather data for pincode: {}, date: {}", pincode, forDate, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
