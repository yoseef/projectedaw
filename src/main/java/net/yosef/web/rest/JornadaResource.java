package net.yosef.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.yosef.domain.Grup;
import net.yosef.domain.Jornada;
import net.yosef.repository.GrupRepository;
import net.yosef.repository.JornadaRepository;
import net.yosef.repository.PartitRepository;
import net.yosef.repository.search.JornadaSearchRepository;
import net.yosef.security.AuthoritiesConstants;
import net.yosef.service.lliga.GeneradorJornades;
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

import org.joda.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Jornada.
 */
@RestController
@RequestMapping("/api")
public class JornadaResource {

    private final Logger log = LoggerFactory.getLogger(JornadaResource.class);

    @Inject
    private JornadaRepository jornadaRepository;

    @Inject
    private JornadaSearchRepository jornadaSearchRepository;

    @Inject
    private GrupRepository grupRepository;

    @Inject
    private PartitRepository partitRepository;

    /**
     * POST  /jornadas -> Create a new jornada.
     */
    @Secured({AuthoritiesConstants.ADMIN})
    @RequestMapping(value = "/jornadas",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Jornada jornada) throws URISyntaxException {
        log.debug("REST request to save Jornada : {}", jornada);
        if (jornada.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new jornada cannot already have an ID").build();
        }
        jornadaRepository.save(jornada);
        jornadaSearchRepository.save(jornada);
        return ResponseEntity.created(new URI("/api/jornadas/" + jornada.getId())).build();
    }

    /**
     * PUT  /jornadas -> Updates an existing jornada.
     */
    @Secured({AuthoritiesConstants.ADMIN})
    @RequestMapping(value = "/jornadas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Jornada jornada) throws URISyntaxException {
        log.debug("REST request to update Jornada : {}", jornada);
        if (jornada.getId() == null) {
            return create(jornada);
        }
        jornadaRepository.save(jornada);
        jornadaSearchRepository.save(jornada);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /jornadas -> get all the jornadas.
     */
    @RequestMapping(value = "/jornadas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Jornada> getAll() {
        log.debug("REST request to get all Jornadas");
        return jornadaRepository.findAll();
    }


    /**
     * GET  /jornadas/:id -> get the "id" jornada.
     */
    @RequestMapping(value = "/jornadas/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Jornada> get(@PathVariable Long id) {
        log.debug("REST request to get Jornada : {}", id);
        return Optional.ofNullable(jornadaRepository.findOne(id))
            .map(jornada -> new ResponseEntity<>(
                jornada,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /jornadas/generar/:id -> get the "id" jornada.
     */
    @Secured({AuthoritiesConstants.ADMIN})
    @RequestMapping(value = "/jornadas/generar/{id}/{data}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getGenerar(@PathVariable Long id, @PathVariable String data) {
        log.debug("REST request to get Jornada : {}", id);
        //busquem el grup apartir de l'id:
        Grup g = grupRepository.findOne(id);
//        LocalDate d = LocalDate.parse(data);
        LocalDate d = new LocalDate(data);
        if (!g.getJornadas().isEmpty()){
            return ResponseEntity.badRequest().header("Fail","No es pot generar jornades per un equip que ja en te!").build();
        }
        GeneradorJornades generador = new GeneradorJornades(g, d);

        generador.setPartitRepository(partitRepository);
        generador.setJornadaRepository(jornadaRepository);

        if (generador.generarLliga()) {
            return ResponseEntity.ok().header("Ok","S'ha generat les jorandes correctement!").build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * DELETE  /jornadas/:id -> delete the "id" jornada.
     */
    @Secured({AuthoritiesConstants.ADMIN})
    @RequestMapping(value = "/jornadas/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Jornada : {}", id);
        jornadaRepository.delete(id);
        jornadaSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/jornadas/:query -> search for the jornada corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/jornadas/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Jornada> search(@PathVariable String query) {
        return StreamSupport
            .stream(jornadaSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
