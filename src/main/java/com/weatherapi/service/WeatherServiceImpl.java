package com.weatherapi.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.weatherapi.dao.PincodeRepo;
import com.weatherapi.dao.WeatherRepo;
import com.weatherapi.dto.PincodeDto;
import com.weatherapi.entities.Pincode;
import com.weatherapi.entities.Weather;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WeatherServiceImpl implements IWeatherService {
		@Autowired
    private  WeatherRepo weatherRepo;
		@Autowired
    private  PincodeRepo pincodeRepo;
  

    @Value("${weather.api.key}")
    private String weatherApiKey;

    @Value("${weather.api.url}")
    private String weatherApiUrl;



    @Override
    public Weather getWeatherInfo(String pincode, String country, LocalDate date) {
        log.info("WeatherServiceImpl.getWeatherInfo:Fetch pincode details from API for pincode: {}, country: {}", pincode, country);

        Optional<Pincode> pincodeOpt = Optional.ofNullable(getPincodeDetails(pincode, country));
        log.info("WeatherServiceImpl.getWeatherInfo:Fetch weather details from API for pincode: {}, country: {}", pincode, country);

        return pincodeOpt.map(p -> fetchWeatherForPincode(p, date)).orElse(null);
    }

    private Pincode getPincodeDetails(String pincode, String country) {
        log.info("WeatherServiceImpl.getPincodeDetails:Fetch pincode details from API for pincode: {}, country: {}", pincode, country);
        return pincodeRepo.findByPincode(pincode)
                .orElseGet(() -> {
                    Pincode newPincode = fetchPincodeDetailsFromApi(pincode, country);
                    if (newPincode != null) {
                        pincodeRepo.save(newPincode);
                    }
                    return newPincode;
                });
    }

    private Pincode fetchPincodeDetailsFromApi(String pincode, String country) {
        log.info("WeatherServiceImpl.fetchPincodeDetailsFromApi:Fetch weather details from API for pincode: {}, country: {}", pincode, country);

        String geoApiUrl = String.format("%s/geo/1.0/zip?zip=%s,%s&appid=%s", weatherApiUrl, pincode, country, weatherApiKey);

        try {
        	RestTemplate restTemplate = new RestTemplate();
            PincodeDto pincodeRes = restTemplate.getForObject(geoApiUrl, PincodeDto.class);
            if (pincodeRes != null) {
            	Pincode newPincode = new Pincode();
                newPincode.setPincode(pincode);
                newPincode.setLatitude(pincodeRes.getLat());
                newPincode.setLongitude(pincodeRes.getLon());
                return newPincode;
            }
        } catch (Exception e) {
            log.error("WeatherServiceImpl.fetchPincodeDetailsFromApi:Failed to fetch pincode details from API for pincode: {}, country: {}", pincode, country, e);
        }
        return null;
    }

    private Weather fetchWeatherForPincode(Pincode pincode, LocalDate date) {
        log.info("WeatherServiceImpl.fetchWeatherForPincode:Fetch weather details from API for pincode: {}, date: {}", pincode.getPincode(), date);

        Optional<Weather> optionalWeather = weatherRepo.findByPincodeAndForDate(pincode, date);
        if (optionalWeather.isPresent()) {
            return optionalWeather.get();
        }

        if (date.equals(LocalDate.now())) {
            String weatherApiRequest = String.format("%s/data/2.5/weather?lat=%s&lon=%s&appid=%s", 
                weatherApiUrl, pincode.getLatitude(), pincode.getLongitude(), weatherApiKey);

            try {
                RestTemplate restTemplate = new RestTemplate();
                String weatherApiRes = restTemplate.getForObject(weatherApiRequest, String.class);
                
                if (weatherApiRes != null) {
                    // Save the new data only if it hasn't been saved already
                    Weather weather = new Weather();
                    weather.setPincode(pincode);
                    weather.setForDate(date);
                    weather.setWeatherData(weatherApiRes);
                    weatherRepo.save(weather);
                    return weather;
                }
            } catch (Exception e) {
                log.error("WeatherServiceImpl.fetchWeatherForPincode: Failed to fetch weather data from API for pincode: {}, date: {}", pincode.getPincode(), date, e);
            }
        }
        
        return null;
    }
}
