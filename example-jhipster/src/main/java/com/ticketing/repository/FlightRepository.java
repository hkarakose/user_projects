package com.ticketing.repository;

import com.ticketing.domain.Flight;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Flight entity.
 */
@SuppressWarnings("unused")
public interface FlightRepository extends JpaRepository<Flight,Long> {

}
