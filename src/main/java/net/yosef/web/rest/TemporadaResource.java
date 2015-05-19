package net.yosef.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.yosef.domain.Temporada;
import net.yosef.repository.TemporadaRepository;
import net.yosef.repository.search.TemporadaSearchRepository;
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
 * REST controller for managing Temporada.
 */
@RestController
@RequestMapping("/api")
public class TemporadaResource {

    private final Logger log = LoggerFactory.getLogger(TemporadaResource.class);

    @Inject
    private TemporadaRepository temporadaRepository;

    @Inject
    private TemporadaSearchRepository temporadaSearchRepository;

    /**
     * POST  /temporadas -> Create a new temporada.
     */
    @RequestMapping(value = "/temporadas",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Temporada temporada) throws URISyntaxException {
        log.debug("REST request to save Temporada : {}", temporada);
        if (temporada.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new temporada cannot already have an ID").build();
        }
        temporadaRepository.save(temporada);
        temporadaSearchRepository.save(temporada);
        return ResponseEntity.created(new URI("/api/temporadas/" + temporada.getId())).build();
    }

    /**
     * PUT  /temporadas -> Updates an existing temporada.
     */
    @RequestMapping(value = "/temporadas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Temporada temporada) throws URISyntaxException {
        log.debug("REST request to update Temporada : {}", temporada);
        if (temporada.getId() == null) {
            return create(temporada);
        }
        temporadaRepository.save(temporada);
        temporadaSearchRepository.save(temporada);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /temporadas -> get all the temporadas.
     */
    @RequestMapping(value = "/temporadas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Temporada> getAll() {
        log.debug("REST request to get all Temporadas");
        return temporadaRepository.findAll();
    }

    /**
     * GET  /temporadas/:id -> get the "id" temporada.
     */
    @RequestMapping(value = "/temporadas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Temporada> get(@PathVariable Long id) {
        log.debug("REST request to get Temporada : {}", id);
        return Optional.ofNullable(temporadaRepository.findOne(id))
            .map(temporada -> new ResponseEntity<>(
                temporada,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /temporadas/:id -> delete the "id" temporada.
     */
    @RequestMapping(value = "/temporadas/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Temporada : {}", id);
        temporadaRepository.delete(id);
        temporadaSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/temporadas/:query -> search for the temporada corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/temporadas/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Temporada> search(@PathVariable String query) {
        return StreamSupport
            .stream(temporadaSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
