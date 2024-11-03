package com.weatherapi.entities;


import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Table(name = "weather")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @Column(name = "pincode", nullable = false)
//    private String pincode;
    @ManyToOne
    @JoinColumn(name = "pincode", referencedColumnName = "pincode", nullable = false)
    private Pincode pincode;

    @Column(name = "for_date", nullable = false)
    private LocalDate forDate;

    @Column(name = "weather_data", columnDefinition = "JSON", nullable = false)
    private String weatherData;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate createdAt = LocalDate.now();
}
