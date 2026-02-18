package invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    private final DBConnection dbConnection;

    public DataRetriever() {
        this.dbConnection = new DBConnection();
    }

    public List<InvoiceTotal> findInvoiceTotals() {

        String sql = """
                SELECT
                    i.id                                AS invoice_id,
                    i.customer_name,
                    i.status::TEXT                      AS status,
                    SUM(il.quantity * il.unit_price)    AS total
                FROM invoice i
                JOIN invoice_line il ON il.invoice_id = i.id
                GROUP BY i.id, i.customer_name, i.status
                ORDER BY i.id
                """;

        List<InvoiceTotal> results = new ArrayList<>();
        Connection conn = dbConnection.getDBConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(new InvoiceTotal(
                        rs.getInt("invoice_id"),
                        rs.getString("customer_name"),
                        rs.getString("status"),
                        rs.getBigDecimal("total").setScale(2)
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur findInvoiceTotals", e);
        } finally {
            dbConnection.closeConnection(conn);
        }

        return results;
    }
}