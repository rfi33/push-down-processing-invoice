SELECT
    invoice_line.invoice_id,
    invoice.customer_name,
    SUM(invoice_line.quantity * invoice_line.unit_price) AS total
FROM invoice
         JOIN invoice_line ON invoice_line.invoice_id = invoice.id
GROUP BY invoice_line.invoice_id, invoice.customer_name
ORDER BY invoice_line.invoice_id;
