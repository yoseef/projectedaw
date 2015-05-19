package net.yosef.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.yosef.domain.Franja;
import net.yosef.repository.FranjaRepository;
import net.yosef.repository.search.FranjaSearchRepository;
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
 * REST controller for managing Franja.
 */
@RestController
@RequestMapping("/api")
public class FranjaResource {

    private final Logger log = LoggerFactory.getLogger(FranjaResource.class);

    @Inject
    private FranjaRepository franjaRepository;

    @Inject
    private FranjaSearchRepository franjaSearchRepository;

    /**
     * POST  /franjas -> Create a new franja.
     */
    @RequestMapping(value = "/franjas",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Franja franja) throws URISyntaxException {
        log.debug("REST request to save Franja : {}", franja);
        if (franja.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new franja cannot already have an ID").build();
        }
        franjaRepository.save(franja);
        franjaSearchRepository.save(franja);
        return ResponseEntity.created(new URI("/api/franjas/" + franja.getId())).build();
    }

    /**
     * PUT  /franjas -> Updates an existing franja.
     */
    @RequestMapping(value = "/franjas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Franja franja) throws URISyntaxException {
        log.debug("REST request to update Franja : {}", franja);
        if (franja.getId() == null) {
            return create(franja);
        }
        franjaRepository.save(franja);
        franjaSearchRepository.save(franja);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /franjas -> get all the franjas.
     */
    @RequestMapping(value = "/franjas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Franja> getAll() {
        log.debug("REST request to get all Franjas");
        return franjaRepository.findAll();
    }

    /**
     * GET  /franjas/:id -> get the "id" franja.
     */
    @RequestMapping(value = "/franjas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Franja> get(@PathVariable Long id) {
        log.debug("REST request to get Franja : {}", id);
        return Optional.ofNullable(franjaRepository.findOne(id))
            .map(franja -> new ResponseEntity<>(
                franja,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /franjas/:id -> delete the "id" franja.
     */
    @RequestMapping(value = "/franjas/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Franja : {}", id);
        franjaRepository.delete(id);
        franjaSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/franjas/:query -> search for the franja corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/franjas/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Franja> search(@PathVariable String query) {
        return StreamSupport
            .stream(franjaSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
