package net.yosef.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.yosef.domain.Equip;
import net.yosef.domain.Jugador;
import net.yosef.repository.JugadorRepository;
import net.yosef.repository.search.JugadorSearchRepository;
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

import net.yosef.repository.EquipRepository;
import net.yosef.repository.search.EquipSearchRepository;

/**
 * REST controller for managing Jugador.
 */
@RestController
@RequestMapping("/api")
public class JugadorResource {

    private final Logger log = LoggerFactory.getLogger(JugadorResource.class);

    @Inject
    private JugadorRepository jugadorRepository;

    @Inject
    private JugadorSearchRepository jugadorSearchRepository;

    @Inject
    private EquipRepository equipRepository;

    @Inject
    private EquipSearchRepository equipSearchRepository;


    /**
     * POST  /jugadors -> Create a new jugador.
     */
    @Secured({AuthoritiesConstants.ADMIN,AuthoritiesConstants.CAPITA})
    @RequestMapping(value = "/jugadors",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Jugador jugador) throws URISyntaxException {
        log.debug("REST request to save Jugador : {}", jugador);
        if (jugador.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new jugador cannot already have an ID").build();
        }
        jugadorRepository.save(jugador);
        jugadorSearchRepository.save(jugador);
        if (jugador.getEquip() != null){
            Long idEquip = jugador.getEquip().getId();
            Equip e = equipRepository.findOne(idEquip);
            e.addJugador(jugador);
            equipRepository.save(e);
            equipSearchRepository.save(e);
        }
        return ResponseEntity.created(new URI("/api/jugadors/" + jugador.getId())).build();
    }

    /**
     * PUT  /jugadors -> Updates an existing jugador.
     */
    @Secured({AuthoritiesConstants.ADMIN,AuthoritiesConstants.CAPITA})
    @RequestMapping(value = "/jugadors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Jugador jugador) throws URISyntaxException {
        log.debug("REST request to update Jugador : {}", jugador);
        if (jugador.getId() == null) {
            return create(jugador);
        }
        jugadorRepository.save(jugador);
        jugadorSearchRepository.save(jugador);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /jugadors -> get all the jugadors.
     */
    @RequestMapping(value = "/jugadors",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Jugador> getAll() {
        log.debug("REST request to get all Jugadors");
        return jugadorRepository.findAll();
    }

    /**
     * GET  /jugadors/:id -> get the "id" jugador.
     */
    @RequestMapping(value = "/jugadors/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Jugador> get(@PathVariable Long id) {
        log.debug("REST request to get Jugador : {}", id);
        return Optional.ofNullable(jugadorRepository.findOne(id))
            .map(jugador -> new ResponseEntity<>(
                jugador,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /jugadors/:id -> delete the "id" jugador.
     */
    @Secured({AuthoritiesConstants.ADMIN,AuthoritiesConstants.CAPITA})
    @RequestMapping(value = "/jugadors/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Jugador : {}", id);
        jugadorRepository.delete(id);
        jugadorSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/jugadors/:query -> search for the jugador corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/jugadors/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Jugador> search(@PathVariable String query) {
        return StreamSupport
            .stream(jugadorSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
