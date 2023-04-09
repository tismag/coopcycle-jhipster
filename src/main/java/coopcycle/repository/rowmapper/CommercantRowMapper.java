package coopcycle.repository.rowmapper;

import coopcycle.domain.Commercant;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Commercant}, with proper type conversions.
 */
@Service
public class CommercantRowMapper implements BiFunction<Row, String, Commercant> {

    private final ColumnConverter converter;

    public CommercantRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Commercant} stored in the database.
     */
    @Override
    public Commercant apply(Row row, String prefix) {
        Commercant entity = new Commercant();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdCommercant(converter.fromRow(row, prefix + "_id_commercant", Long.class));
        entity.setNom(converter.fromRow(row, prefix + "_nom", String.class));
        entity.setPrenom(converter.fromRow(row, prefix + "_prenom", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setNumeroTel(converter.fromRow(row, prefix + "_numero_tel", String.class));
        entity.setNomEtablissement(converter.fromRow(row, prefix + "_nom_etablissement", String.class));
        entity.setAdresse(converter.fromRow(row, prefix + "_adresse", String.class));
        return entity;
    }
}
