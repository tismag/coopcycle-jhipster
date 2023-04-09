package coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CommercantSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("id_commercant", table, columnPrefix + "_id_commercant"));
        columns.add(Column.aliased("nom", table, columnPrefix + "_nom"));
        columns.add(Column.aliased("prenom", table, columnPrefix + "_prenom"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));
        columns.add(Column.aliased("numero_tel", table, columnPrefix + "_numero_tel"));
        columns.add(Column.aliased("nom_etablissement", table, columnPrefix + "_nom_etablissement"));
        columns.add(Column.aliased("adresse", table, columnPrefix + "_adresse"));

        return columns;
    }
}
