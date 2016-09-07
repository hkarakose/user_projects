package com.ticketing.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AirplaneModel.
 */
@Entity
@Table(name = "airplane_model")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "airplanemodel")
public class AirplaneModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "model", nullable = false)
    private String model;

    @ManyToOne
    private Airplane airplane;

    @ManyToOne
    private AirplaneModelSeat airplaneModelSeat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public AirplaneModel model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public AirplaneModel airplane(Airplane airplane) {
        this.airplane = airplane;
        return this;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }

    public AirplaneModelSeat getAirplaneModelSeat() {
        return airplaneModelSeat;
    }

    public AirplaneModel airplaneModelSeat(AirplaneModelSeat airplaneModelSeat) {
        this.airplaneModelSeat = airplaneModelSeat;
        return this;
    }

    public void setAirplaneModelSeat(AirplaneModelSeat airplaneModelSeat) {
        this.airplaneModelSeat = airplaneModelSeat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AirplaneModel airplaneModel = (AirplaneModel) o;
        if(airplaneModel.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, airplaneModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AirplaneModel{" +
            "id=" + id +
            ", model='" + model + "'" +
            '}';
    }
}
