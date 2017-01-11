package com.ticketing.repository;

import com.ticketing.domain.Airport;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Airport entity.
 */
@SuppressWarnings("unused")
public interface AirportRepository extends JpaRepository<Airport,Long> {

}
