package invoice;

import java.math.BigDecimal;
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
                    invoice.id,
                    invoice.customer_name,
                    invoice.status::TEXT,
                    SUM(invoice_line.quantity * invoice_line.unit_price)
                FROM invoice
                JOIN invoice_line ON invoice_line.invoice_id = invoice.id
                GROUP BY invoice.id, invoice.customer_name, invoice.status
                ORDER BY invoice.id
                """;

        List<InvoiceTotal> results = new ArrayList<>();
        Connection conn = dbConnection.getDBConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(new InvoiceTotal(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("status"),
                        rs.getBigDecimal("sum").setScale(2)
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur findInvoiceTotals", e);
        } finally {
            dbConnection.closeConnection(conn);
        }

        return results;
    }

    public List<InvoiceTotal> findConfirmedAndPaidInvoiceTotals() {

        String sql = """
                SELECT
                    invoice.id,
                    invoice.customer_name,
                    invoice.status::TEXT,
                    SUM(invoice_line.quantity * invoice_line.unit_price)
                FROM invoice
                JOIN invoice_line ON invoice_line.invoice_id = invoice.id
                WHERE invoice.status = 'CONFIRMED' OR invoice.status = 'PAID'
                GROUP BY invoice.id, invoice.customer_name, invoice.status
                ORDER BY invoice.id
                """;

        List<InvoiceTotal> results = new ArrayList<>();
        Connection conn = dbConnection.getDBConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(new InvoiceTotal(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("status"),
                        rs.getBigDecimal("sum").setScale(2)
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur findConfirmedAndPaidInvoiceTotals", e);
        } finally {
            dbConnection.closeConnection(conn);
        }

        return results;
    }

    public InvoiceStatusTotals computeStatusTotals() {

        String sql = """
                SELECT
                    SUM(invoice_line.quantity * invoice_line.unit_price)
                        FILTER (WHERE invoice.status = 'PAID')      AS total_paid,
                    SUM(invoice_line.quantity * invoice_line.unit_price)
                        FILTER (WHERE invoice.status = 'CONFIRMED') AS total_confirmed,
                    SUM(invoice_line.quantity * invoice_line.unit_price)
                        FILTER (WHERE invoice.status = 'DRAFT')     AS total_draft
                FROM invoice
                JOIN invoice_line ON invoice_line.invoice_id = invoice.id
                """;

        Connection conn = dbConnection.getDBConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new InvoiceStatusTotals(
                        rs.getBigDecimal("total_paid").setScale(2),
                        rs.getBigDecimal("total_confirmed").setScale(2),
                        rs.getBigDecimal("total_draft").setScale(2)
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur computeStatusTotals", e);
        } finally {
            dbConnection.closeConnection(conn);
        }

        return null;
    }

    public Double computeWeightedTurnover() {

        String sql = """
                SELECT
                    SUM(invoice_line.quantity * invoice_line.unit_price *
                        CASE invoice.status
                            WHEN 'PAID'      THEN 1.0
                            WHEN 'CONFIRMED' THEN 0.5
                            ELSE 0.0
                        END
                    ) AS weighted_turnover
                FROM invoice
                JOIN invoice_line ON invoice_line.invoice_id = invoice.id
                """;

        Connection conn = dbConnection.getDBConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("weighted_turnover");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur computeWeightedTurnover", e);
        } finally {
            dbConnection.closeConnection(conn);
        }

        return 0.0;
    }

    public List<InvoiceTaxSummary> findInvoiceTaxSummaries() {

        String sql = """
                SELECT
                    invoice.id,
                    SUM(invoice_line.quantity * invoice_line.unit_price)                         AS total_ht,
                    SUM(invoice_line.quantity * invoice_line.unit_price) * tax_config.rate / 100 AS total_tva,
                    SUM(invoice_line.quantity * invoice_line.unit_price) * (1 + tax_config.rate / 100) AS total_ttc
                FROM invoice
                JOIN invoice_line ON invoice_line.invoice_id = invoice.id
                CROSS JOIN tax_config
                GROUP BY invoice.id, tax_config.rate
                ORDER BY invoice.id
                """;

        List<InvoiceTaxSummary> results = new ArrayList<>();
        Connection conn = dbConnection.getDBConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(new InvoiceTaxSummary(
                        rs.getInt("id"),
                        rs.getBigDecimal("total_ht").setScale(2),
                        rs.getBigDecimal("total_tva").setScale(2),
                        rs.getBigDecimal("total_ttc").setScale(2)
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur findInvoiceTaxSummaries", e);
        } finally {
            dbConnection.closeConnection(conn);
        }

        return results;
    }


    public BigDecimal computeWeightedTurnoverTtc() {

        String sql = """
                SELECT
                    ROUND(SUM(
                        invoice_line.quantity * invoice_line.unit_price
                        * (1 + tax_config.rate / 100)
                        * CASE invoice.status
                            WHEN 'PAID'      THEN 1.0
                            WHEN 'CONFIRMED' THEN 0.5
                            ELSE 0.0
                          END
                    ), 2) AS weighted_turnover_ttc
                FROM invoice
                JOIN invoice_line ON invoice_line.invoice_id = invoice.id
                CROSS JOIN tax_config
                """;

        Connection conn = dbConnection.getDBConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal("weighted_turnover_ttc").setScale(2);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur computeWeightedTurnoverTtc", e);
        } finally {
            dbConnection.closeConnection(conn);
        }

        return BigDecimal.ZERO.setScale(2);
    }

}