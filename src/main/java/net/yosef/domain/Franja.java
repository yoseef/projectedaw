package net.yosef.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.yosef.domain.util.CustomLocalDateSerializer;
import net.yosef.domain.util.ISO8601LocalDateDeserializer;
import net.yosef.domain.util.CustomDateTimeDeserializer;
import net.yosef.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.DateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Franja.
 */
@Entity
@Table(name = "FRANJA")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="franja")
public class Franja implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "dia")
    private LocalDate dia;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "hora_inici")
    private DateTime hora_inici;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "hora_fi")
    private DateTime hora_fi;

    @OneToOne(mappedBy = "franja")
    private Partit partit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public DateTime getHora_inici() {
        return hora_inici;
    }

    public void setHora_inici(DateTime hora_inici) {
        this.hora_inici = hora_inici;
    }

    public DateTime getHora_fi() {
        return hora_fi;
    }

    public void setHora_fi(DateTime hora_fi) {
        this.hora_fi = hora_fi;
    }

    public Partit getPartit() {
        return partit;
    }

    public void setPartit(Partit partit) {
        this.partit = partit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Franja franja = (Franja) o;

        if ( ! Objects.equals(id, franja.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Franja{" +
                "id=" + id +
                ", dia='" + dia + "'" +
                ", hora_inici='" + hora_inici + "'" +
                ", hora_fi='" + hora_fi + "'" +
                '}';
    }
}
