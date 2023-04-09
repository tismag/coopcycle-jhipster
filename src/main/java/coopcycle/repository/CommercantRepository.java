package coopcycle.repository;

import coopcycle.domain.Commercant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Commercant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommercantRepository extends ReactiveCrudRepository<Commercant, Long>, CommercantRepositoryInternal {
    @Override
    Mono<Commercant> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Commercant> findAllWithEagerRelationships();

    @Override
    Flux<Commercant> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM commercant entity JOIN rel_commercant__id_commercant joinTable ON entity.id = joinTable.id_commercant_id WHERE joinTable.id_commercant_id = :id"
    )
    Flux<Commercant> findByIdCommercant(Long id);

    @Override
    <S extends Commercant> Mono<S> save(S entity);

    @Override
    Flux<Commercant> findAll();

    @Override
    Mono<Commercant> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CommercantRepositoryInternal {
    <S extends Commercant> Mono<S> save(S entity);

    Flux<Commercant> findAllBy(Pageable pageable);

    Flux<Commercant> findAll();

    Mono<Commercant> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Commercant> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Commercant> findOneWithEagerRelationships(Long id);

    Flux<Commercant> findAllWithEagerRelationships();

    Flux<Commercant> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
