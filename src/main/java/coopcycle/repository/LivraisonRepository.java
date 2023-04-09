package coopcycle.repository;

import coopcycle.domain.Livraison;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Livraison entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LivraisonRepository extends ReactiveCrudRepository<Livraison, Long>, LivraisonRepositoryInternal {
    @Query("SELECT * FROM livraison entity WHERE entity.livreur_id = :id")
    Flux<Livraison> findByLivreur(Long id);

    @Query("SELECT * FROM livraison entity WHERE entity.livreur_id IS NULL")
    Flux<Livraison> findAllWhereLivreurIsNull();

    @Override
    <S extends Livraison> Mono<S> save(S entity);

    @Override
    Flux<Livraison> findAll();

    @Override
    Mono<Livraison> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface LivraisonRepositoryInternal {
    <S extends Livraison> Mono<S> save(S entity);

    Flux<Livraison> findAllBy(Pageable pageable);

    Flux<Livraison> findAll();

    Mono<Livraison> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Livraison> findAllBy(Pageable pageable, Criteria criteria);

}
