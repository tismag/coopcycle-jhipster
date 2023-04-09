package coopcycle.web.rest;

import coopcycle.repository.LivraisonRepository;
import coopcycle.service.LivraisonService;
import coopcycle.service.dto.LivraisonDTO;
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
 * REST controller for managing {@link coopcycle.domain.Livraison}.
 */
@RestController
@RequestMapping("/api")
public class LivraisonResource {

    private final Logger log = LoggerFactory.getLogger(LivraisonResource.class);

    private static final String ENTITY_NAME = "livraison";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LivraisonService livraisonService;

    private final LivraisonRepository livraisonRepository;

    public LivraisonResource(LivraisonService livraisonService, LivraisonRepository livraisonRepository) {
        this.livraisonService = livraisonService;
        this.livraisonRepository = livraisonRepository;
    }

    /**
     * {@code POST  /livraisons} : Create a new livraison.
     *
     * @param livraisonDTO the livraisonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new livraisonDTO, or with status {@code 400 (Bad Request)} if the livraison has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/livraisons")
    public Mono<ResponseEntity<LivraisonDTO>> createLivraison(@RequestBody LivraisonDTO livraisonDTO) throws URISyntaxException {
        log.debug("REST request to save Livraison : {}", livraisonDTO);
        if (livraisonDTO.getId() != null) {
            throw new BadRequestAlertException("A new livraison cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return livraisonService
            .save(livraisonDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/livraisons/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /livraisons/:id} : Updates an existing livraison.
     *
     * @param id the id of the livraisonDTO to save.
     * @param livraisonDTO the livraisonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livraisonDTO,
     * or with status {@code 400 (Bad Request)} if the livraisonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the livraisonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/livraisons/{id}")
    public Mono<ResponseEntity<LivraisonDTO>> updateLivraison(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LivraisonDTO livraisonDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Livraison : {}, {}", id, livraisonDTO);
        if (livraisonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livraisonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return livraisonRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return livraisonService
                    .update(livraisonDTO)
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
     * {@code PATCH  /livraisons/:id} : Partial updates given fields of an existing livraison, field will ignore if it is null
     *
     * @param id the id of the livraisonDTO to save.
     * @param livraisonDTO the livraisonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livraisonDTO,
     * or with status {@code 400 (Bad Request)} if the livraisonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the livraisonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the livraisonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/livraisons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<LivraisonDTO>> partialUpdateLivraison(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LivraisonDTO livraisonDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Livraison partially : {}, {}", id, livraisonDTO);
        if (livraisonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livraisonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return livraisonRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<LivraisonDTO> result = livraisonService.partialUpdate(livraisonDTO);

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
     * {@code GET  /livraisons} : get all the livraisons.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of livraisons in body.
     */
    @GetMapping("/livraisons")
    public Mono<List<LivraisonDTO>> getAllLivraisons() {
        log.debug("REST request to get all Livraisons");
        return livraisonService.findAll().collectList();
    }

    /**
     * {@code GET  /livraisons} : get all the livraisons as a stream.
     * @return the {@link Flux} of livraisons.
     */
    @GetMapping(value = "/livraisons", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<LivraisonDTO> getAllLivraisonsAsStream() {
        log.debug("REST request to get all Livraisons as a stream");
        return livraisonService.findAll();
    }

    /**
     * {@code GET  /livraisons/:id} : get the "id" livraison.
     *
     * @param id the id of the livraisonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the livraisonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/livraisons/{id}")
    public Mono<ResponseEntity<LivraisonDTO>> getLivraison(@PathVariable Long id) {
        log.debug("REST request to get Livraison : {}", id);
        Mono<LivraisonDTO> livraisonDTO = livraisonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livraisonDTO);
    }

    /**
     * {@code DELETE  /livraisons/:id} : delete the "id" livraison.
     *
     * @param id the id of the livraisonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/livraisons/{id}")
    public Mono<ResponseEntity<Void>> deleteLivraison(@PathVariable Long id) {
        log.debug("REST request to delete Livraison : {}", id);
        return livraisonService
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
