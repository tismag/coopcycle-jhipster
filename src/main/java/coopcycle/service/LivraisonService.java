package coopcycle.service;

import coopcycle.domain.Livraison;
import coopcycle.repository.LivraisonRepository;
import coopcycle.service.dto.LivraisonDTO;
import coopcycle.service.mapper.LivraisonMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Livraison}.
 */
@Service
@Transactional
public class LivraisonService {

    private final Logger log = LoggerFactory.getLogger(LivraisonService.class);

    private final LivraisonRepository livraisonRepository;

    private final LivraisonMapper livraisonMapper;

    public LivraisonService(LivraisonRepository livraisonRepository, LivraisonMapper livraisonMapper) {
        this.livraisonRepository = livraisonRepository;
        this.livraisonMapper = livraisonMapper;
    }

    /**
     * Save a livraison.
     *
     * @param livraisonDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LivraisonDTO> save(LivraisonDTO livraisonDTO) {
        log.debug("Request to save Livraison : {}", livraisonDTO);
        return livraisonRepository.save(livraisonMapper.toEntity(livraisonDTO)).map(livraisonMapper::toDto);
    }

    /**
     * Update a livraison.
     *
     * @param livraisonDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LivraisonDTO> update(LivraisonDTO livraisonDTO) {
        log.debug("Request to update Livraison : {}", livraisonDTO);
        return livraisonRepository.save(livraisonMapper.toEntity(livraisonDTO)).map(livraisonMapper::toDto);
    }

    /**
     * Partially update a livraison.
     *
     * @param livraisonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<LivraisonDTO> partialUpdate(LivraisonDTO livraisonDTO) {
        log.debug("Request to partially update Livraison : {}", livraisonDTO);

        return livraisonRepository
            .findById(livraisonDTO.getId())
            .map(existingLivraison -> {
                livraisonMapper.partialUpdate(existingLivraison, livraisonDTO);

                return existingLivraison;
            })
            .flatMap(livraisonRepository::save)
            .map(livraisonMapper::toDto);
    }

    /**
     * Get all the livraisons.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<LivraisonDTO> findAll() {
        log.debug("Request to get all Livraisons");
        return livraisonRepository.findAll().map(livraisonMapper::toDto);
    }

    /**
     * Returns the number of livraisons available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return livraisonRepository.count();
    }

    /**
     * Get one livraison by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<LivraisonDTO> findOne(Long id) {
        log.debug("Request to get Livraison : {}", id);
        return livraisonRepository.findById(id).map(livraisonMapper::toDto);
    }

    /**
     * Delete the livraison by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Livraison : {}", id);
        return livraisonRepository.deleteById(id);
    }
}
