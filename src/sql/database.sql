INSERT INTO invoice (id,customer_name, status) VALUES
(1, 'Alice', 'CONFIRMED'),
(2, 'Bob', 'PAID'),
(3, 'Charlie', 'DRAFT');
INSERT INTO invoice_line (id,invoice_id, label, quantity, unit_price) VALUES
(1,1, 'Produit A', 2, 100),
(2,1, 'Produit B', 1, 50),
(3, 2,'Produit A', 5, 100),
(4,3, 'Service C', 1, 200),
(5, 3,'Produit B', 3, 50);

INSERT INTO tax_config (label, rate) VALUES
('TVA STANDARD', 20);
