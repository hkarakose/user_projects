package com.ticketing.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Airplane.
 */
@Entity
@Table(name = "airplane")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "airplane")
public class Airplane implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private AirplaneModel airplaneModel;

    @ManyToOne
    private Airlines airlines;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Airplane name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AirplaneModel getAirplaneModel() {
        return airplaneModel;
    }

    public Airplane airplaneModel(AirplaneModel airplaneModel) {
        this.airplaneModel = airplaneModel;
        return this;
    }

    public void setAirplaneModel(AirplaneModel airplaneModel) {
        this.airplaneModel = airplaneModel;
    }

    public Airlines getAirlines() {
        return airlines;
    }

    public Airplane airlines(Airlines airlines) {
        this.airlines = airlines;
        return this;
    }

    public void setAirlines(Airlines airlines) {
        this.airlines = airlines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Airplane airplane = (Airplane) o;
        if(airplane.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, airplane.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Airplane{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
