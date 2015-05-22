package net.yosef.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.yosef.domain.Grup;
import net.yosef.repository.GrupRepository;
import net.yosef.repository.search.GrupSearchRepository;
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
 * REST controller for managing Grup.
 */
@RestController
@RequestMapping("/api")
public class GrupResource {

    private final Logger log = LoggerFactory.getLogger(GrupResource.class);

    @Inject
    private GrupRepository grupRepository;

    @Inject
    private GrupSearchRepository grupSearchRepository;

    /**
     * POST  /grups -> Create a new grup.
     */
    @Secured({AuthoritiesConstants.ADMIN})
    @RequestMapping(value = "/grups",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Grup grup) throws URISyntaxException {
        log.debug("REST request to save Grup : {}", grup);
        if (grup.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new grup cannot already have an ID").build();
        }
        grupRepository.save(grup);
        grupSearchRepository.save(grup);
        return ResponseEntity.created(new URI("/api/grups/" + grup.getId())).build();
    }

    /**
     * PUT  /grups -> Updates an existing grup.
     */
    @Secured({AuthoritiesConstants.ADMIN})
    @RequestMapping(value = "/grups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Grup grup) throws URISyntaxException {
        log.debug("REST request to update Grup : {}", grup);
        if (grup.getId() == null) {
            return create(grup);
        }
        grupRepository.save(grup);
        grupSearchRepository.save(grup);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /grups -> get all the grups.
     */
    @RequestMapping(value = "/grups",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Grup> getAll() {
        log.debug("REST request to get all Grups");
        return grupRepository.findAll();
    }

    /**
     * GET  /grups -> get all the grups.
     */
    @RequestMapping(value = "/grupsByTemp",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Grup> getAllByTemp() {
        log.debug("REST request to get all Grups by Temp");
        return grupRepository.findAll();
    }

    /**
     * GET  /grups/:id -> get the "id" grup.
     */
    @RequestMapping(value = "/grups/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Grup> get(@PathVariable Long id) {
        log.debug("REST request to get Grup : {}", id);
        return Optional.ofNullable(grupRepository.findOne(id))
            .map(grup -> new ResponseEntity<>(
                grup,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /grups/:id -> delete the "id" grup.
     */
    @Secured({AuthoritiesConstants.ADMIN})
    @RequestMapping(value = "/grups/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Grup : {}", id);
        grupRepository.delete(id);
        grupSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/grups/:query -> search for the grup corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/grups/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Grup> search(@PathVariable String query) {
        return StreamSupport
            .stream(grupSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
