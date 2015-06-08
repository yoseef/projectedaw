package net.yosef.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.yosef.domain.Grup;
import net.yosef.domain.Partit;
import net.yosef.repository.ClassificacioRepository;
import net.yosef.repository.EquipRepository;
import net.yosef.repository.PartitRepository;
import net.yosef.repository.search.PartitSearchRepository;
import net.yosef.security.AuthoritiesConstants;
import net.yosef.service.classificacio.Calcul;
import net.yosef.validator.PartitValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
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

    @Inject
    private EquipRepository equipRepository;

    @Inject
    private ClassificacioRepository classificacioRepository;

    @Autowired
    private PartitValidator partitValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(partitValidator);
    }

    /**
     * POST  /partits -> Create a new partit.
     */
    @Secured({AuthoritiesConstants.ADMIN})
    @RequestMapping(value = "/partits",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Partit partit, BindingResult result) throws URISyntaxException {
        log.debug("REST request to save Partit : {}", partit);
        if (partit.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new partit cannot already have an ID").build();
        }
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().header("Failure", "Errrrrrrror").build();
        }
        partitRepository.save(partit);
        partitSearchRepository.save(partit);
        return ResponseEntity.created(new URI("/api/partits/" + partit.getId())).build();

    }

    /**
     * PUT  /partits -> Updates an existing partit.
     */
    @Secured({AuthoritiesConstants.ADMIN})
    @RequestMapping(value = "/partits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Partit partit, BindingResult result) throws URISyntaxException {
        log.debug("REST request to update Partit : {}", partit);
        if (partit.getId() == null) {
            return create(partit, result);
        }
        if (partit.getGols_l() == null || partit.getGols_v() == null) {
            return ResponseEntity.badRequest().header("Fail","revisa els camps gol").build();
        }

        if (partit.getGols_l() < -1 || partit.getGols_v() < -1) {
            return ResponseEntity.badRequest().header("Fail","revisa els camps gols han der posit").build();
        }

        partitRepository.save(partit);
        partitSearchRepository.save(partit);

        //calcular punts:
        Calcul c = new Calcul();
        c.setEquipRepository(equipRepository);
        c.setClassificacioRepository(classificacioRepository);
        c.decidirGuanyador(partit);

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
    @Secured({AuthoritiesConstants.ADMIN})
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
