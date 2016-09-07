package com.ticketing.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Airlines.
 */
@Entity
@Table(name = "airlines")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "airlines")
public class Airlines implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "abbreviation")
    private String abbreviation;

    @ManyToOne
    private Airplane airplane;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Airlines name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public Airlines abbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
        return this;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public Airlines airplane(Airplane airplane) {
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
        Airlines airlines = (Airlines) o;
        if(airlines.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, airlines.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Airlines{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", abbreviation='" + abbreviation + "'" +
            '}';
    }
}
