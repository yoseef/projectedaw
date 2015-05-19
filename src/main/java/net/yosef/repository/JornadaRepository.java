package net.yosef.repository;

import net.yosef.domain.Jornada;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Jornada entity.
 */
public interface JornadaRepository extends JpaRepository<Jornada,Long> {

}
