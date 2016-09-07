package com.ticketing.repository;

import com.ticketing.domain.Airplane;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Airplane entity.
 */
@SuppressWarnings("unused")
public interface AirplaneRepository extends JpaRepository<Airplane,Long> {

}
