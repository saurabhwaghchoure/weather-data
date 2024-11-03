package com.weatherapi.service;

import java.time.LocalDate;

import com.weatherapi.entities.Weather;

public interface IWeatherService {

	Weather getWeatherInfo(String pincode,String country,	LocalDate date);
}
