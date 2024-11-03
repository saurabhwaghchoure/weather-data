package com.weatherapi.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weatherapi.entities.Pincode;

@Repository
public interface PincodeRepo extends JpaRepository<Pincode, Long> {

    Optional<Pincode> findByPincode(String pincode);

}
