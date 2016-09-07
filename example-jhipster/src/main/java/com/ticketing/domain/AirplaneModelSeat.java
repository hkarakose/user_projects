package com.ticketing.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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

    @Column(name = "model_id")
    private Long modelId;

    @NotNull
    @Column(name = "seat_no", nullable = false)
    private String seatNo;

    @OneToMany(mappedBy = "airplaneModelSeat")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AirplaneModel> modelIds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getModelId() {
        return modelId;
    }

    public AirplaneModelSeat modelId(Long modelId) {
        this.modelId = modelId;
        return this;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
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

    public Set<AirplaneModel> getModelIds() {
        return modelIds;
    }

    public AirplaneModelSeat modelIds(Set<AirplaneModel> airplaneModels) {
        this.modelIds = airplaneModels;
        return this;
    }

    public AirplaneModelSeat addAirplaneModel(AirplaneModel airplaneModel) {
        modelIds.add(airplaneModel);
        airplaneModel.setAirplaneModelSeat(this);
        return this;
    }

    public AirplaneModelSeat removeAirplaneModel(AirplaneModel airplaneModel) {
        modelIds.remove(airplaneModel);
        airplaneModel.setAirplaneModelSeat(null);
        return this;
    }

    public void setModelIds(Set<AirplaneModel> airplaneModels) {
        this.modelIds = airplaneModels;
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
            ", modelId='" + modelId + "'" +
            ", seatNo='" + seatNo + "'" +
            '}';
    }
}
