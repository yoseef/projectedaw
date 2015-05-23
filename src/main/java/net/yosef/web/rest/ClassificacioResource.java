package net.yosef.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.yosef.domain.Classificacio;
import net.yosef.repository.ClassificacioRepository;
import net.yosef.repository.search.ClassificacioSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
 * REST controller for managing Classificacio.
 */
@RestController
@RequestMapping("/api")
public class ClassificacioResource {

    private final Logger log = LoggerFactory.getLogger(ClassificacioResource.class);

    @Inject
    private ClassificacioRepository classificacioRepository;

    @Inject
    private ClassificacioSearchRepository classificacioSearchRepository;

    /**
     * POST  /classificacios -> Create a new classificacio.
     */
    @RequestMapping(value = "/classificacios",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Classificacio classificacio) throws URISyntaxException {
        log.debug("REST request to save Classificacio : {}", classificacio);
        if (classificacio.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new classificacio cannot already have an ID").build();
        }
        classificacioRepository.save(classificacio);
        classificacioSearchRepository.save(classificacio);
        return ResponseEntity.created(new URI("/api/classificacios/" + classificacio.getId())).build();
    }

    /**
     * PUT  /classificacios -> Updates an existing classificacio.
     */
    @RequestMapping(value = "/classificacios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Classificacio classificacio) throws URISyntaxException {
        log.debug("REST request to update Classificacio : {}", classificacio);
        if (classificacio.getId() == null) {
            return create(classificacio);
        }
        classificacioRepository.save(classificacio);
        classificacioSearchRepository.save(classificacio);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /classificacios -> get all the classificacios.
     */
    @RequestMapping(value = "/classificacios",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Classificacio> getAll() {
        log.debug("REST request to get all Classificacios");
        return classificacioRepository.findAll();
    }

    /**
     * GET  /classificacios/:id -> get the "id" classificacio.
     */
    @RequestMapping(value = "/classificacios/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Classificacio> get(@PathVariable Long id) {
        log.debug("REST request to get Classificacio : {}", id);
        return Optional.ofNullable(classificacioRepository.findOne(id))
            .map(classificacio -> new ResponseEntity<>(
                classificacio,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /classificacios/:id -> delete the "id" classificacio.
     */
    @RequestMapping(value = "/classificacios/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Classificacio : {}", id);
        classificacioRepository.delete(id);
        classificacioSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/classificacios/:query -> search for the classificacio corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/classificacios/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Classificacio> search(@PathVariable String query) {
        return StreamSupport
            .stream(classificacioSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
