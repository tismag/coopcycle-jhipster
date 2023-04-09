package coopcycle.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import coopcycle.domain.Commercant;
import coopcycle.domain.Cooperative;
import coopcycle.repository.rowmapper.CommercantRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Commercant entity.
 */
@SuppressWarnings("unused")
class CommercantRepositoryInternalImpl extends SimpleR2dbcRepository<Commercant, Long> implements CommercantRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CommercantRowMapper commercantMapper;

    private static final Table entityTable = Table.aliased("commercant", EntityManager.ENTITY_ALIAS);

    private static final EntityManager.LinkTable idCommercantLink = new EntityManager.LinkTable(
        "rel_commercant__id_commercant",
        "commercant_id",
        "id_commercant_id"
    );

    public CommercantRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CommercantRowMapper commercantMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Commercant.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.commercantMapper = commercantMapper;
    }

    @Override
    public Flux<Commercant> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Commercant> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CommercantSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Commercant.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Commercant> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Commercant> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Commercant> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Commercant> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Commercant> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Commercant process(Row row, RowMetadata metadata) {
        Commercant entity = commercantMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Commercant> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Commercant> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(idCommercantLink, entity.getId(), entity.getIdCommercants().stream().map(Cooperative::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(idCommercantLink, entityId);
    }
}
