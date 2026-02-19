SELECT
    invoice.id,
    invoice.customer_name,
    SUM(invoice_line.quantity * invoice_line.unit_price) AS total
FROM invoice
         JOIN invoice_line ON invoice_line.invoice_id = invoice.id
GROUP BY invoice.id, invoice.customer_name
ORDER BY invoice.id;

SELECT
    invoice.id,
    invoice.customer_name,
    invoice.status::TEXT,
    SUM(invoice_line.quantity * invoice_line.unit_price)
FROM invoice
         JOIN invoice_line ON invoice_line.invoice_id = invoice.id
WHERE invoice.status = 'CONFIRMED' OR invoice.status = 'PAID'
GROUP BY invoice.id, invoice.customer_name, invoice.status
ORDER BY invoice.id;

SELECT
    SUM(CASE WHEN invoice.status = 'PAID' THEN invoice_line.quantity * invoice_line.unit_price ELSE 0 END) AS total_paid,
    SUM(CASE WHEN invoice.status = 'CONFIRMED' THEN invoice_line.quantity * invoice_line.unit_price ELSE 0 END) AS total_confirmed,
    SUM(CASE WHEN invoice.status = 'DRAFT' THEN invoice_line.quantity * invoice_line.unit_price ELSE 0 END) AS total_draft
FROM invoice
         JOIN invoice_line ON invoice_line.invoice_id = invoice.id;

SELECT
    SUM(invoice_line.quantity * invoice_line.unit_price *
        CASE invoice.status
            WHEN 'PAID'      THEN 1.0
            WHEN 'CONFIRMED' THEN 0.5
            ELSE 0.0
            END
    ) AS weighted_turnover
FROM invoice
         JOIN invoice_line ON invoice_line.invoice_id = invoice.id;

SELECT
    invoice.id,
    SUM(invoice_line.quantity * invoice_line.unit_price)                    AS total_ht,
    SUM(invoice_line.quantity * invoice_line.unit_price) * tax_config.rate / 100 AS total_tva,
    SUM(invoice_line.quantity * invoice_line.unit_price) * (1 + tax_config.rate / 100) AS total_ttc
FROM invoice
         JOIN invoice_line ON invoice_line.invoice_id = invoice.id
         CROSS JOIN tax_config
GROUP BY invoice.id, tax_config.rate
ORDER BY invoice.id;

SELECT
    SUM(
            invoice_line.quantity * invoice_line.unit_price
                * (1 + tax_config.rate / 100)
                * CASE invoice.status
                      WHEN 'PAID'      THEN 1.0
                      WHEN 'CONFIRMED' THEN 0.5
                      ELSE 0.0
                END
    ) AS weighted_turnover_ttc
FROM invoice
         JOIN invoice_line ON invoice_line.invoice_id = invoice.id
         CROSS JOIN tax_config;