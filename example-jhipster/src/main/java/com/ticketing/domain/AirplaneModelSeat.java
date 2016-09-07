package com.ticketing.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AirplaneModelSeat.
 */
@Entity
@Table(name = "airplane_model_seat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "airplanemodelseat")
public class AirplaneModelSeat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "seat_no", nullable = false)
    private String seatNo;

    @ManyToOne
    private AirplaneModel model;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public AirplaneModelSeat seatNo(String seatNo) {
        this.seatNo = seatNo;
        return this;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public AirplaneModel getModel() {
        return model;
    }

    public AirplaneModelSeat model(AirplaneModel airplaneModel) {
        this.model = airplaneModel;
        return this;
    }

    public void setModel(AirplaneModel airplaneModel) {
        this.model = airplaneModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AirplaneModelSeat airplaneModelSeat = (AirplaneModelSeat) o;
        if(airplaneModelSeat.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, airplaneModelSeat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AirplaneModelSeat{" +
            "id=" + id +
            ", seatNo='" + seatNo + "'" +
            '}';
    }
}
