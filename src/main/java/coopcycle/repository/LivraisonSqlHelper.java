package coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LivraisonSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("id_livraison", table, columnPrefix + "_id_livraison"));
        columns.add(Column.aliased("contenu", table, columnPrefix + "_contenu"));
        columns.add(Column.aliased("quantite", table, columnPrefix + "_quantite"));

        columns.add(Column.aliased("livreur_id", table, columnPrefix + "_livreur_id"));
        return columns;
    }
}
