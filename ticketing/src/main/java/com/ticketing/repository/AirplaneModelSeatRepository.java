package com.ticketing.repository;

import com.ticketing.domain.AirplaneModelSeat;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AirplaneModelSeat entity.
 */
@SuppressWarnings("unused")
public interface AirplaneModelSeatRepository extends JpaRepository<AirplaneModelSeat,Long> {

}
