package com.ticketing.repository;

import com.ticketing.domain.AirplaneModel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AirplaneModel entity.
 */
@SuppressWarnings("unused")
public interface AirplaneModelRepository extends JpaRepository<AirplaneModel,Long> {

}
