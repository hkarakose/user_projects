package com.ticketing.repository;

import com.ticketing.domain.Airlines;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Airlines entity.
 */
@SuppressWarnings("unused")
public interface AirlinesRepository extends JpaRepository<Airlines,Long> {

}
