package coopcycle.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import coopcycle.domain.Livraison;
import coopcycle.repository.rowmapper.LivraisonRowMapper;
import coopcycle.repository.rowmapper.LivreurRowMapper;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Livraison entity.
 */
@SuppressWarnings("unused")
class LivraisonRepositoryInternalImpl extends SimpleR2dbcRepository<Livraison, Long> implements LivraisonRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final LivreurRowMapper livreurMapper;
    private final LivraisonRowMapper livraisonMapper;

    private static final Table entityTable = Table.aliased("livraison", EntityManager.ENTITY_ALIAS);
    private static final Table livreurTable = Table.aliased("livreur", "livreur");

    public LivraisonRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        LivreurRowMapper livreurMapper,
        LivraisonRowMapper livraisonMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Livraison.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.livreurMapper = livreurMapper;
        this.livraisonMapper = livraisonMapper;
    }

    @Override
    public Flux<Livraison> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Livraison> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = LivraisonSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(LivreurSqlHelper.getColumns(livreurTable, "livreur"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(livreurTable)
            .on(Column.create("livreur_id", entityTable))
            .equals(Column.create("id", livreurTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Livraison.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Livraison> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Livraison> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Livraison process(Row row, RowMetadata metadata) {
        Livraison entity = livraisonMapper.apply(row, "e");
        entity.setLivreur(livreurMapper.apply(row, "livreur"));
        return entity;
    }

    @Override
    public <S extends Livraison> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
