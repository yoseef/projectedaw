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
 * A Classificacio.
 */
@Entity
@Table(name = "CLASSIFICACIO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="classificacio")
public class Classificacio implements Serializable {

    public Classificacio() {
    }

    public Classificacio(Grup grup, Temporada temporada) {
        this.grup = grup;
        this.temporada = temporada;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @NotNull
//    @Column(name = "punts", nullable = false)
//    private Integer punts;

    @ManyToOne
    private Temporada temporada;

    @ManyToOne
    private Grup grup;

    @OneToMany(mappedBy = "classificacio",fetch = FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Equip> equips = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Integer getPunts() {
//        return punts;
//    }
//
//    public void setPunts(Integer punts) {
//        this.punts = punts;
//    }

    public Temporada getTemporada() {
        return temporada;
    }

    public void setTemporada(Temporada temporada) {
        this.temporada = temporada;
    }

    public Grup getGrup() {
        return grup;
    }

    public void setGrup(Grup grup) {
        this.grup = grup;
    }

    public Set<Equip> getEquips() {
        return equips;
    }

    public void setEquips(Set<Equip> equips) {
        this.equips = equips;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Classificacio classificacio = (Classificacio) o;

        if ( ! Objects.equals(id, classificacio.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Classificacio{" +
                "id=" + id +
                '}';
    }
}
