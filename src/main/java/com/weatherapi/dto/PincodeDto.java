package com.weatherapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PincodeDto {
    private String zip;
    private String name;
    private double lat;
    private double lon;
    private String country;
}