package net.yosef.repository;

import net.yosef.domain.Grup;
import net.yosef.domain.Temporada;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Grup entity.
 */
public interface GrupRepository extends JpaRepository<Grup,Long> {
    List<Grup> findByTemporada(Temporada t);
}
