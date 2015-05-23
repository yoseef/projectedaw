package net.yosef.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.yosef.domain.Equip;
import net.yosef.repository.EquipRepository;
import net.yosef.repository.search.EquipSearchRepository;
import net.yosef.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Equip.
 */
@RestController
@RequestMapping("/api")
public class EquipResource {

    private final Logger log = LoggerFactory.getLogger(EquipResource.class);

    @Inject
    private EquipRepository equipRepository;

    @Inject
    private EquipSearchRepository equipSearchRepository;

    /**
     * POST  /equips -> Create a new equip.
     */
    @Secured({AuthoritiesConstants.ADMIN,AuthoritiesConstants.USER})
    @RequestMapping(value = "/equips",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Equip equip) throws URISyntaxException {
        log.debug("REST request to save Equip : {}", equip);
        if (equip.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new equip cannot already have an ID").build();
        }
        equipRepository.save(equip);
        equipSearchRepository.save(equip);

        return ResponseEntity.created(new URI("/api/equips/" + equip.getId())).build();
    }

    /**
     * PUT  /equips -> Updates an existing equip.
     */
    @Secured({AuthoritiesConstants.ADMIN,AuthoritiesConstants.CAPITA})
    @RequestMapping(value = "/equips",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Equip equip) throws URISyntaxException {
        log.debug("REST request to update Equip : {}", equip);
        if (equip.getId() == null) {
            return create(equip);
        }
        equipRepository.save(equip);
        equipSearchRepository.save(equip);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /equips -> get all the equips.
     */
    @RequestMapping(value = "/equips",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Equip> getAll() {
        log.debug("REST request to get all Equips");
        return equipRepository.findAll();
    }

    /**
     * GET  /equips/:id -> get the "id" equip.
     */
    @RequestMapping(value = "/equips/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Equip> get(@PathVariable Long id) {
        log.debug("REST request to get Equip : {}", id);
        return Optional.ofNullable(equipRepository.findOne(id))
            .map(equip -> new ResponseEntity<>(
                equip,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /equips/:id -> delete the "id" equip.
     */
    @Secured({AuthoritiesConstants.ADMIN,AuthoritiesConstants.CAPITA})
    @RequestMapping(value = "/equips/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Equip : {}", id);
        equipRepository.delete(id);
        equipSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/equips/:query -> search for the equip corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/equips/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Equip> search(@PathVariable String query) {
        return StreamSupport
            .stream(equipSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
