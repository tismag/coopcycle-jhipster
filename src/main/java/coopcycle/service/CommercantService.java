package coopcycle.service;

import coopcycle.domain.Commercant;
import coopcycle.repository.CommercantRepository;
import coopcycle.service.dto.CommercantDTO;
import coopcycle.service.mapper.CommercantMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Commercant}.
 */
@Service
@Transactional
public class CommercantService {

    private final Logger log = LoggerFactory.getLogger(CommercantService.class);

    private final CommercantRepository commercantRepository;

    private final CommercantMapper commercantMapper;

    public CommercantService(CommercantRepository commercantRepository, CommercantMapper commercantMapper) {
        this.commercantRepository = commercantRepository;
        this.commercantMapper = commercantMapper;
    }

    /**
     * Save a commercant.
     *
     * @param commercantDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CommercantDTO> save(CommercantDTO commercantDTO) {
        log.debug("Request to save Commercant : {}", commercantDTO);
        return commercantRepository.save(commercantMapper.toEntity(commercantDTO)).map(commercantMapper::toDto);
    }

    /**
     * Update a commercant.
     *
     * @param commercantDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CommercantDTO> update(CommercantDTO commercantDTO) {
        log.debug("Request to update Commercant : {}", commercantDTO);
        return commercantRepository.save(commercantMapper.toEntity(commercantDTO)).map(commercantMapper::toDto);
    }

    /**
     * Partially update a commercant.
     *
     * @param commercantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CommercantDTO> partialUpdate(CommercantDTO commercantDTO) {
        log.debug("Request to partially update Commercant : {}", commercantDTO);

        return commercantRepository
            .findById(commercantDTO.getId())
            .map(existingCommercant -> {
                commercantMapper.partialUpdate(existingCommercant, commercantDTO);

                return existingCommercant;
            })
            .flatMap(commercantRepository::save)
            .map(commercantMapper::toDto);
    }

    /**
     * Get all the commercants.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CommercantDTO> findAll() {
        log.debug("Request to get all Commercants");
        return commercantRepository.findAll().map(commercantMapper::toDto);
    }

    /**
     * Get all the commercants with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<CommercantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return commercantRepository.findAllWithEagerRelationships(pageable).map(commercantMapper::toDto);
    }

    /**
     * Returns the number of commercants available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return commercantRepository.count();
    }

    /**
     * Get one commercant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CommercantDTO> findOne(Long id) {
        log.debug("Request to get Commercant : {}", id);
        return commercantRepository.findOneWithEagerRelationships(id).map(commercantMapper::toDto);
    }

    /**
     * Delete the commercant by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Commercant : {}", id);
        return commercantRepository.deleteById(id);
    }
}
