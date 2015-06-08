package net.yosef.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.yosef.domain.Authority;
import net.yosef.domain.User;
import net.yosef.repository.UserRepository;
import net.yosef.repository.search.UserSearchRepository;
import net.yosef.security.AuthoritiesConstants;
import net.yosef.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private UserService userService;

    /**
     * GET  /users -> get all users.
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> getAll() {
        log.debug("REST request to get all Users");
//        return userRepository.findAll();
        return userRepository.findAll();

    }

    /**
     * GET  /users/:login -> get the "login" user.
     */
    @RequestMapping(value = "/users/{login}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<User> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userRepository.findOneByLogin(login)
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /users/:id -> delete the "id" user.
     */
    @RequestMapping(value = "/users/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete User : {}", id);
//        es l'usuari actual que ha solicitat la operacio?

        if(userService.getUserWithAuthorities().getId() == id){
            userRepository.delete(id);
            userSearchRepository.delete(id);
        }
    }


    /**
     * SEARCH  /_search/users/:query -> search for the User corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/users/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> search(@PathVariable String query) {
        return StreamSupport
            .stream(userSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
