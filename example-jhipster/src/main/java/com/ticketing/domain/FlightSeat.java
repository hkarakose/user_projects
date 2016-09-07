package com.ticketing.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FlightSeat.
 */
@Entity
@Table(name = "flight_seat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "flightseat")
public class FlightSeat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "seat_no", nullable = false)
    private String seatNo;

    @Column(name = "availability")
    private Boolean availability;

    @ManyToOne
    private Flight flight;

    @ManyToOne
    private User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public FlightSeat seatNo(String seatNo) {
        this.seatNo = seatNo;
        return this;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public Boolean isAvailability() {
        return availability;
    }

    public FlightSeat availability(Boolean availability) {
        this.availability = availability;
        return this;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public Flight getFlight() {
        return flight;
    }

    public FlightSeat flight(Flight flight) {
        this.flight = flight;
        return this;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public User getOwner() {
        return owner;
    }

    public FlightSeat owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlightSeat flightSeat = (FlightSeat) o;
        if(flightSeat.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, flightSeat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FlightSeat{" +
            "id=" + id +
            ", seatNo='" + seatNo + "'" +
            ", availability='" + availability + "'" +
            '}';
    }
}
