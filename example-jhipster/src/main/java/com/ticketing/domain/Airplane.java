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

    @Column(name = "airplane_model_id")
    private Long airplaneModelId;

    @NotNull
    @Column(name = "airlines_id", nullable = false)
    private Long airlinesId;

    @OneToMany(mappedBy = "airplane")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Airlines> airlinesIds = new HashSet<>();

    @OneToMany(mappedBy = "airplane")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AirplaneModel> airplaneModelIds = new HashSet<>();

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

    public Long getAirplaneModelId() {
        return airplaneModelId;
    }

    public Airplane airplaneModelId(Long airplaneModelId) {
        this.airplaneModelId = airplaneModelId;
        return this;
    }

    public void setAirplaneModelId(Long airplaneModelId) {
        this.airplaneModelId = airplaneModelId;
    }

    public Long getAirlinesId() {
        return airlinesId;
    }

    public Airplane airlinesId(Long airlinesId) {
        this.airlinesId = airlinesId;
        return this;
    }

    public void setAirlinesId(Long airlinesId) {
        this.airlinesId = airlinesId;
    }

    public Set<Airlines> getAirlinesIds() {
        return airlinesIds;
    }

    public Airplane airlinesIds(Set<Airlines> airlines) {
        this.airlinesIds = airlines;
        return this;
    }

    public Airplane addAirlines(Airlines airlines) {
        airlinesIds.add(airlines);
        airlines.setAirplane(this);
        return this;
    }

    public Airplane removeAirlines(Airlines airlines) {
        airlinesIds.remove(airlines);
        airlines.setAirplane(null);
        return this;
    }

    public void setAirlinesIds(Set<Airlines> airlines) {
        this.airlinesIds = airlines;
    }

    public Set<AirplaneModel> getAirplaneModelIds() {
        return airplaneModelIds;
    }

    public Airplane airplaneModelIds(Set<AirplaneModel> airplaneModels) {
        this.airplaneModelIds = airplaneModels;
        return this;
    }

    public Airplane addAirplaneModel(AirplaneModel airplaneModel) {
        airplaneModelIds.add(airplaneModel);
        airplaneModel.setAirplane(this);
        return this;
    }

    public Airplane removeAirplaneModel(AirplaneModel airplaneModel) {
        airplaneModelIds.remove(airplaneModel);
        airplaneModel.setAirplane(null);
        return this;
    }

    public void setAirplaneModelIds(Set<AirplaneModel> airplaneModels) {
        this.airplaneModelIds = airplaneModels;
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
            ", airplaneModelId='" + airplaneModelId + "'" +
            ", airlinesId='" + airlinesId + "'" +
            '}';
    }
}
