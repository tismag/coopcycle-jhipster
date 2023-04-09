package coopcycle.repository.rowmapper;

import coopcycle.domain.Cooperative;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Cooperative}, with proper type conversions.
 */
@Service
public class CooperativeRowMapper implements BiFunction<Row, String, Cooperative> {

    private final ColumnConverter converter;

    public CooperativeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Cooperative} stored in the database.
     */
    @Override
    public Cooperative apply(Row row, String prefix) {
        Cooperative entity = new Cooperative();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdCooperative(converter.fromRow(row, prefix + "_id_cooperative", Long.class));
        entity.setNom(converter.fromRow(row, prefix + "_nom", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setNumeroTel(converter.fromRow(row, prefix + "_numero_tel", String.class));
        entity.setAdresse(converter.fromRow(row, prefix + "_adresse", String.class));
        return entity;
    }
}
