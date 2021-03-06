package net.yosef.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Equip.
 */
@Entity
@Table(name = "EQUIP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "equip")
public class Equip implements Serializable {
    @NotNull
    @Size(min = 3)
    @Column(name = "nom", nullable = false)
    private String nom;

    public Equip(String n, LocalDate d, Grup g) {
        nom = n;
        data_alta = d;
        grup = g;
        gols_favor= 0;
        gols_contra = 0;
        pj = 0;
        pg = 0;
        pe = 0;
        pp = 0;
        pts=0;
    }

    public Equip() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @NotNull
//    @Size(min = 3)
//    @Column(name = "nom", nullable = false)
//    private String nom;

    @Column(name = "gols_favor")
    private Integer gols_favor;

    @Column(name = "gols_contra")
    private Integer gols_contra;

    @Column(name = "pj")
    private Integer pj;

    @Column(name = "pg")
    private Integer pg;

    @Column(name = "pe")
    private Integer pe;

    @Column(name = "pp")
    private Integer pp;

    @Column(name = "punts")
    private Integer pts;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "data_alta", nullable = false)
    private LocalDate data_alta;

    @Column(name = "pagat")
    private Boolean pagat;

    @NotNull
    @ManyToOne
    private Grup grup;

    @ManyToMany(mappedBy = "equips")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Partit> partits = new HashSet<>();

    @OneToMany(mappedBy = "equip")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Jugador> jugadors = new HashSet<>();

    @OneToOne(mappedBy="equip")
    @JsonIgnore
    private User user;

    @ManyToOne
    private Classificacio classificacio;

    public void addJugador(Jugador j) {
        if (jugadors != null && j != null)
            jugadors.add(j);
    }

    public void removeJugador(Jugador j) {
        if (jugadors != null && j != null)
            jugadors.remove(j);
    }

    public void haGuanyat(int gols_f, int gols_c) {
        _Setgols_f_c(gols_f, gols_c);
        pj++;
        pg++;
        pts += 3;
    }

    public void haEmpatat(int gols_f) {
        _Setgols_f_c(gols_f, gols_f);
        pj++;
        pe++;
        pts++;
    }

    public void haPerdut(int gols_f, int gols_c) {
        _Setgols_f_c(gols_f, gols_c);
        pj++;
        pp++;
    }

    public void _Setgols_f_c(int gols_f, int gols_c) {
        gols_contra = +gols_c;
        gols_favor = +gols_f;
    }

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

    public Integer getGols_favor() {
        return gols_favor;
    }

    public void setGols_favor(Integer gols_favor) {
        this.gols_favor = gols_favor;
    }

    public Integer getGols_contra() {
        return gols_contra;
    }

    public void setGols_contra(Integer gols_contra) {
        this.gols_contra = gols_contra;
    }

    public Integer getPj() {
        return pj;
    }

    public void setPj(Integer pj) {
        this.pj = pj;
    }

    public Integer getPg() {
        return pg;
    }

    public void setPg(Integer pg) {
        this.pg = pg;
    }

    public Integer getPe() {
        return pe;
    }

    public void setPe(Integer pe) {
        this.pe = pe;
    }

    public Integer getPp() {
        return pp;
    }

    public void setPp(Integer pp) {
        this.pp = pp;
    }

    public Integer getPts() {
        return pts;
    }

    public void setPts(Integer pts) {
        this.pts = pts;
    }

    public LocalDate getData_alta() {
        return data_alta;
    }

    public void setData_alta(LocalDate data_alta) {
        this.data_alta = data_alta;
    }

    public Boolean getPagat() {
        return pagat;
    }

    public void setPagat(Boolean pagat) {
        this.pagat = pagat;
    }

    public Grup getGrup() {
        return grup;
    }

    public void setGrup(Grup grup) {
        this.grup = grup;
    }

    public Set<Partit> getPartits() {
        return partits;
    }

    public void setPartits(Set<Partit> partits) {
        this.partits = partits;
    }

    public Set<Jugador> getJugadors() {
        return jugadors;
    }

    public void setJugadors(Set<Jugador> jugadors) {
        this.jugadors = jugadors;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Classificacio getClassificacio() {
        return classificacio;
    }

    public void setClassificacio(Classificacio classificacio) {
        this.classificacio = classificacio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Equip equip = (Equip) o;

        if (!Objects.equals(id, equip.id)) return false;
        if (!Objects.equals(nom, equip.nom)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Equip{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", gols_favor='" + gols_favor + "'" +
            ", gols_contra='" + gols_contra + "'" +
            ", pj='" + pj + "'" +
            ", pg='" + pg + "'" +
            ", pe='" + pe + "'" +
            ", pp='" + pp + "'" +
            ", data_alta='" + data_alta + "'" +
            ", pagat='" + pagat + "'" +
            '}';
    }
}
