package com.weatherapi.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.weatherapi.entities.Pincode;
import com.weatherapi.entities.Weather;

public interface WeatherRepo extends JpaRepository<Weather, Long> {

    @Query("SELECT w FROM Weather w WHERE w.pincode.pincode = :pincode")
    List<Weather> findWeatherByPincode(@Param("pincode") String pincode);
    
    Optional<Weather> findByPincodeAndForDate(Pincode pincode, LocalDate forDate);
}
