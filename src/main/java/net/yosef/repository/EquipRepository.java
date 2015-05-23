package net.yosef.repository;

import net.yosef.domain.Equip;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Equip entity.
 */
public interface EquipRepository extends JpaRepository<Equip,Long> {

}
