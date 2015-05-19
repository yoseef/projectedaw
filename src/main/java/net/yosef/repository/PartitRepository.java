package net.yosef.repository;

import net.yosef.domain.Partit;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Partit entity.
 */
public interface PartitRepository extends JpaRepository<Partit,Long> {

    @Query("select partit from Partit partit left join fetch partit.equips where partit.id =:id")
    Partit findOneWithEagerRelationships(@Param("id") Long id);

}
