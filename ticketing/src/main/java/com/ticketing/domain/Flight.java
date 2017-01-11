package com.ticketing.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.ticketing.domain.enumeration.Status;

/**
 * A Flight.
 */
@Entity
@Table(name = "flight")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "flight")
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "flight_code", nullable = false)
    private String flightCode;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    private Airport departure;

    @ManyToOne
    private Airport arrival;

    @ManyToOne
    private Airplane airplane;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public Flight flightCode(String flightCode) {
        this.flightCode = flightCode;
        return this;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Flight date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public Flight status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Airport getDeparture() {
        return departure;
    }

    public Flight departure(Airport airport) {
        this.departure = airport;
        return this;
    }

    public void setDeparture(Airport airport) {
        this.departure = airport;
    }

    public Airport getArrival() {
        return arrival;
    }

    public Flight arrival(Airport airport) {
        this.arrival = airport;
        return this;
    }

    public void setArrival(Airport airport) {
        this.arrival = airport;
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public Flight airplane(Airplane airplane) {
        this.airplane = airplane;
        return this;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Flight flight = (Flight) o;
        if(flight.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, flight.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Flight{" +
            "id=" + id +
            ", flightCode='" + flightCode + "'" +
            ", date='" + date + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
