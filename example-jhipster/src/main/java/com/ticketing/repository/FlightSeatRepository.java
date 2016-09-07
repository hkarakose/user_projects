package com.ticketing.repository;

import com.ticketing.domain.FlightSeat;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FlightSeat entity.
 */
@SuppressWarnings("unused")
public interface FlightSeatRepository extends JpaRepository<FlightSeat,Long> {

    @Query("select flightSeat from FlightSeat flightSeat where flightSeat.owner.login = ?#{principal.username}")
    List<FlightSeat> findByOwnerIsCurrentUser();

}
