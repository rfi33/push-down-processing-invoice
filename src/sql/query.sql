SELECT
    invoice.id,
    invoice.customer_name,
    SUM(invoice_line.quantity * invoice_line.unit_price) AS total
FROM invoice
         JOIN invoice_line ON invoice_line.invoice_id = invoice.id
GROUP BY invoice.id, invoice.customer_name
ORDER BY invoice.id;


SELECT
    invoice_line.invoice_id,
    invoice.customer_name,
    invoice.status,
    SUM(invoice_line.quantity * invoice_line.unit_price) AS total
FROM invoice
         JOIN invoice_line ON invoice_line.invoice_id = invoice.id
WHERE invoice.status IN ('CONFIRMED', 'PAID')
GROUP BY invoice_line.invoice_id, invoice.customer_name, invoice.status
ORDER BY invoice_line.invoice_id;

SELECT
    SUM(CASE WHEN invoice.status = 'PAID' THEN invoice_line.quantity * invoice_line.unit_price ELSE 0 END) AS total_paid,
    SUM(CASE WHEN invoice.status = 'CONFIRMED' THEN invoice_line.quantity * invoice_line.unit_price ELSE 0 END) AS total_confirmed,
    SUM(CASE WHEN invoice.status = 'DRAFT' THEN invoice_line.quantity * invoice_line.unit_price ELSE 0 END) AS total_draft
FROM invoice
         JOIN invoice_line ON invoice_line.invoice_id = invoice.id;
