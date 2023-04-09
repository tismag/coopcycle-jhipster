package coopcycle.web.rest;

import coopcycle.repository.CommercantRepository;
import coopcycle.service.CommercantService;
import coopcycle.service.dto.CommercantDTO;
import coopcycle.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link coopcycle.domain.Commercant}.
 */
@RestController
@RequestMapping("/api")
public class CommercantResource {

    private final Logger log = LoggerFactory.getLogger(CommercantResource.class);

    private static final String ENTITY_NAME = "commercant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommercantService commercantService;

    private final CommercantRepository commercantRepository;

    public CommercantResource(CommercantService commercantService, CommercantRepository commercantRepository) {
        this.commercantService = commercantService;
        this.commercantRepository = commercantRepository;
    }

    /**
     * {@code POST  /commercants} : Create a new commercant.
     *
     * @param commercantDTO the commercantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commercantDTO, or with status {@code 400 (Bad Request)} if the commercant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commercants")
    public Mono<ResponseEntity<CommercantDTO>> createCommercant(@RequestBody CommercantDTO commercantDTO) throws URISyntaxException {
        log.debug("REST request to save Commercant : {}", commercantDTO);
        if (commercantDTO.getId() != null) {
            throw new BadRequestAlertException("A new commercant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return commercantService
            .save(commercantDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/commercants/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /commercants/:id} : Updates an existing commercant.
     *
     * @param id the id of the commercantDTO to save.
     * @param commercantDTO the commercantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commercantDTO,
     * or with status {@code 400 (Bad Request)} if the commercantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commercantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commercants/{id}")
    public Mono<ResponseEntity<CommercantDTO>> updateCommercant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommercantDTO commercantDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Commercant : {}, {}", id, commercantDTO);
        if (commercantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commercantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return commercantRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return commercantService
                    .update(commercantDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /commercants/:id} : Partial updates given fields of an existing commercant, field will ignore if it is null
     *
     * @param id the id of the commercantDTO to save.
     * @param commercantDTO the commercantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commercantDTO,
     * or with status {@code 400 (Bad Request)} if the commercantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commercantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commercantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commercants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CommercantDTO>> partialUpdateCommercant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommercantDTO commercantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Commercant partially : {}, {}", id, commercantDTO);
        if (commercantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commercantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return commercantRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CommercantDTO> result = commercantService.partialUpdate(commercantDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /commercants} : get all the commercants.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commercants in body.
     */
    @GetMapping("/commercants")
    public Mono<List<CommercantDTO>> getAllCommercants(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Commercants");
        return commercantService.findAll().collectList();
    }

    /**
     * {@code GET  /commercants} : get all the commercants as a stream.
     * @return the {@link Flux} of commercants.
     */
    @GetMapping(value = "/commercants", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CommercantDTO> getAllCommercantsAsStream() {
        log.debug("REST request to get all Commercants as a stream");
        return commercantService.findAll();
    }

    /**
     * {@code GET  /commercants/:id} : get the "id" commercant.
     *
     * @param id the id of the commercantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commercantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commercants/{id}")
    public Mono<ResponseEntity<CommercantDTO>> getCommercant(@PathVariable Long id) {
        log.debug("REST request to get Commercant : {}", id);
        Mono<CommercantDTO> commercantDTO = commercantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commercantDTO);
    }

    /**
     * {@code DELETE  /commercants/:id} : delete the "id" commercant.
     *
     * @param id the id of the commercantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commercants/{id}")
    public Mono<ResponseEntity<Void>> deleteCommercant(@PathVariable Long id) {
        log.debug("REST request to delete Commercant : {}", id);
        return commercantService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
