package net.yosef.repository;

import net.yosef.domain.Classificacio;
import net.yosef.domain.Grup;
import net.yosef.domain.Temporada;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Classificacio entity.
 */
public interface ClassificacioRepository extends JpaRepository<Classificacio, Long> {
    Classificacio findByGrupAndTemporada(Grup g, Temporada t);
}
