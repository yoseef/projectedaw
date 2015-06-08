package net.yosef.domain;

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
 * A Grup.
 */
@Entity
@Table(name = "GRUP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "grup")
public class Grup implements Serializable {
    public Grup() {

    }

    public Grup(String n) {
        nom = n;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @ManyToOne
    private Temporada temporada;

    @OneToMany(mappedBy = "grup", fetch = FetchType.EAGER)
    @JsonIgnore
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Equip> equips = new HashSet<>();

    @OneToMany(mappedBy = "grup", fetch = FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Jornada> jornadas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Temporada getTemporada() {
        return temporada;
    }

    public void setTemporada(Temporada temporada) {
        this.temporada = temporada;
    }

    public Set<Equip> getEquips() {
        return equips;
    }

    public void setEquips(Set<Equip> equips) {
        this.equips = equips;
    }

    public Set<Jornada> getJornadas() {
        return jornadas;
    }

    public void setJornadas(Set<Jornada> jornadas) {
        this.jornadas = jornadas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Grup grup = (Grup) o;

        if (!Objects.equals(id, grup.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Grup{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            '}';
    }
}
