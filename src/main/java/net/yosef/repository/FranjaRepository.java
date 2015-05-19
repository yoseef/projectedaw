package net.yosef.repository;

import net.yosef.domain.Franja;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Franja entity.
 */
public interface FranjaRepository extends JpaRepository<Franja,Long> {

}
