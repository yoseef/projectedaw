package net.yosef.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.yosef.domain.util.CustomLocalDateSerializer;
import net.yosef.domain.util.ISO8601LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * A Partit.
 */
@Entity
@Table(name = "PARTIT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "partit")
public class Partit implements Serializable {
    public Partit() {
    }

    public Partit(Equip local, Equip visitant) {
        if (!local.equals(visitant)){
            nom_l = local.getNom();
            nom_v = visitant.getNom();

            equips.put("local", local);
            equips.put("visitant", visitant);

        }

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nom_v")
    private String nom_v;

    @Column(name = "nom_l")
    private String nom_l;

    @Column(name = "gols_v")
    private Integer gols_v;

    @Column(name = "gols_l")
    private Integer gols_l;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "data")
    private LocalDate data;

    @Column(name = "arbitre")
    private String arbitre;

    @ManyToOne
    private Jornada jornada;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "PARTIT_EQUIP",
        joinColumns = @JoinColumn(name = "partits_id", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "equips_id", referencedColumnName = "ID"))
    private Map<String, Equip> equips = new HashMap<>();

//    private Equip local;
//    private Equip visitant;

    @OneToOne
    private Franja franja;

    public Long getId() {

        return id;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom_v() {
        return nom_v;
    }

    public void setNom_v(String nom_v) {
        this.nom_v = nom_v;
    }

    public String getNom_l() {
        return nom_l;
    }

    public void setNom_l(String nom_l) {
        this.nom_l = nom_l;
    }

    public Integer getGols_v() {
        return gols_v;
    }

    public void setGols_v(Integer gols_v) {
        this.gols_v = gols_v;
    }

    public Integer getGols_l() {
        return gols_l;
    }

    public void setGols_l(Integer gols_l) {
        this.gols_l = gols_l;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getArbitre() {
        return arbitre;
    }

    public void setArbitre(String arbitre) {
        this.arbitre = arbitre;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public Map<String, Equip> getEquips() {
        return equips;
    }

    public void setEquips(Map<String, Equip> equips) {
        this.equips = equips;
    }

    public Franja getFranja() {
        return franja;
    }

    public void setFranja(Franja franja) {
        this.franja = franja;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Partit partit = (Partit) o;

        if (!Objects.equals(id, partit.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Partit{" +
            "id=" + id +
            ", nom_v='" + nom_v + "'" +
            ", nom_l='" + nom_l + "'" +
            ", gols_v='" + gols_v + "'" +
            ", gols_l='" + gols_l + "'" +
            ", data='" + data + "'" +
            ", arbitre='" + arbitre + "'" +
            '}';
    }
}
