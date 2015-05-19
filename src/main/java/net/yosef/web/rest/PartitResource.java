package net.yosef.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.yosef.domain.Partit;
import net.yosef.repository.PartitRepository;
import net.yosef.repository.search.PartitSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Partit.
 */
@RestController
@RequestMapping("/api")
public class PartitResource {

    private final Logger log = LoggerFactory.getLogger(PartitResource.class);

    @Inject
    private PartitRepository partitRepository;

    @Inject
    private PartitSearchRepository partitSearchRepository;

    /**
     * POST  /partits -> Create a new partit.
     */
    @RequestMapping(value = "/partits",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Partit partit) throws URISyntaxException {
        log.debug("REST request to save Partit : {}", partit);
        if (partit.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new partit cannot already have an ID").build();
        }
        partitRepository.save(partit);
        partitSearchRepository.save(partit);
        return ResponseEntity.created(new URI("/api/partits/" + partit.getId())).build();
    }

    /**
     * PUT  /partits -> Updates an existing partit.
     */
    @RequestMapping(value = "/partits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Partit partit) throws URISyntaxException {
        log.debug("REST request to update Partit : {}", partit);
        if (partit.getId() == null) {
            return create(partit);
        }
        partitRepository.save(partit);
        partitSearchRepository.save(partit);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /partits -> get all the partits.
     */
    @RequestMapping(value = "/partits",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Partit> getAll() {
        log.debug("REST request to get all Partits");
        return partitRepository.findAll();
    }

    /**
     * GET  /partits/:id -> get the "id" partit.
     */
    @RequestMapping(value = "/partits/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Partit> get(@PathVariable Long id) {
        log.debug("REST request to get Partit : {}", id);
        return Optional.ofNullable(partitRepository.findOneWithEagerRelationships(id))
            .map(partit -> new ResponseEntity<>(
                partit,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /partits/:id -> delete the "id" partit.
     */
    @RequestMapping(value = "/partits/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Partit : {}", id);
        partitRepository.delete(id);
        partitSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/partits/:query -> search for the partit corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/partits/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Partit> search(@PathVariable String query) {
        return StreamSupport
            .stream(partitSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
